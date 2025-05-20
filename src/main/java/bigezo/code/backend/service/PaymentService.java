package bigezo.code.backend.service;

import bigezo.code.backend.model.BillingAddress;
import bigezo.code.backend.model.Payment;
import bigezo.code.backend.repository.PaymentRepository;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import bigezo.code.backend.model.PaymentResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import bigezo.code.backend.model.PaymentStatus;
import bigezo.code.backend.model.Student;
import bigezo.code.backend.service.PaymentStatusService;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private static final String PESAPAL_AUTH_URL = "https://pay.pesapal.com/v3/api/Auth/RequestToken";
    private static final String PESAPAL_PAYMENT_URL = "https://pay.pesapal.com/v3/api/Transactions/SubmitOrderRequest";
    private static final int MAX_AUTH_RETRIES = 3;

    private final PaymentStatusService paymentStatusService;
    private String accessToken = "";
    private LocalDateTime lastAuthTime;

    @Value("${pesapal.consumer.key}")
    private String consumerKey;

    @Value("${pesapal.consumer.secret}")
    private String consumerSecret;

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentService(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
        this.lastAuthTime = LocalDateTime.now();
    }

    private void authenticatePesapal() throws IOException {
        logger.info("Attempting to authenticate with Pesapal...");

        if (StringUtils.hasText(consumerKey) && StringUtils.hasText(consumerSecret)) {
            String authJson = String.format("{\"consumer_key\":\"%s\",\"consumer_secret\":\"%s\"}", consumerKey, consumerSecret);

            RequestBody body = RequestBody.create(authJson, MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(PESAPAL_AUTH_URL)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    String errorBody = response.body() != null ? response.body().string() : "No response body";
                    logger.error("Pesapal authentication failed: {} - {}", response.code(), errorBody);
                    throw new IOException("Failed to authenticate with Pesapal: " + response.code() + " - " + errorBody);
                }

                String responseBody = response.body().string();
                accessToken = objectMapper.readTree(responseBody).path("token").asText();
                lastAuthTime = LocalDateTime.now();
                logger.info("Successfully obtained Pesapal access token......................................................................... Token length: {}", accessToken.length());
            }
        } else {
            logger.error("Missing Pesapal credentials. consumerKey: {} consumerSecret: {}",
                    StringUtils.hasText(consumerKey), StringUtils.hasText(consumerSecret));
            throw new IOException("Missing Pesapal credentials");
        }
    }

    /**
     * Make a payment request to Pesapal
     * @param paymentRequest The payment request details
     * @return The saved payment object
     * @throws IOException if payment processing fails
     */
    public String makePayment(Payment paymentRequest) throws IOException {
        logger.info("Processing payment request: {}", paymentRequest);

        // Validate payment request
        validatePaymentRequest(paymentRequest);

        // Ensure we have a valid access token
        ensureValidAccessToken();

        // Create a copy of the payment request to modify
        Payment requestCopy = new Payment();
        requestCopy.setPesapalId(paymentRequest.getPesapalId());
        requestCopy.setCurrency(paymentRequest.getCurrency());
        requestCopy.setAmount(paymentRequest.getAmount());
        requestCopy.setDescription(paymentRequest.getDescription());
        requestCopy.setCallbackUrl(paymentRequest.getCallbackUrl()); // Make sure this is not null
        requestCopy.setRedirectMode(paymentRequest.getRedirectMode());
        requestCopy.setNotificationId(paymentRequest.getNotificationId());
        requestCopy.setBranch(paymentRequest.getBranch());
        requestCopy.setBillingAddress(paymentRequest.getBillingAddress());

        // Validate callback URL
        if (requestCopy.getCallbackUrl() == null || requestCopy.getCallbackUrl().isEmpty()) {
            logger.error("Callback URL is required but was not provided");
            throw new IllegalArgumentException("Callback URL is required");
        }

        // Create a billing address if it's null
        if (paymentRequest.getBillingAddress() == null) {
            BillingAddress billingAddress = new BillingAddress();
            billingAddress.setEmailAddress("bigezo.com");
            billingAddress.setPhoneNumber("0773913902");
            billingAddress.setCountryCode("UG");
            billingAddress.setFirstName("Random");
            billingAddress.setLastName("User");
            requestCopy.setBillingAddress(billingAddress);
        } else {
            requestCopy.setBillingAddress(paymentRequest.getBillingAddress());
        }

        // Prepare payment request
        String json = objectMapper.writeValueAsString(requestCopy);
        logger.debug("Sending payment request: {}", json);

        // Create request body
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        // Build request
        Request request = new Request.Builder()
                .url(PESAPAL_PAYMENT_URL)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "No response body";
                logger.error("Payment request failed: {} - {}", response.code(), errorBody);
                throw new IOException("Payment request failed: " + response.code() + " - " + errorBody);
            }

            String responseBody = response.body().string();
            logger.info("Payment response received: {}", responseBody);

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                PaymentResponse paymentResponse = objectMapper.readValue(responseBody, PaymentResponse.class);

                logger.info("Payment response received: {}", paymentResponse);

                // Check if the response is successful
                if (paymentResponse.getStatus().equals("200") && paymentResponse.getError() == null) {
                    logger.info("Payment successfully processed");

                    // Create a Payment object from the response
                    Payment payment = new Payment();
                    payment.setPesapalId(paymentResponse.getMerchantReference());
                    payment.setNotificationId(paymentResponse.getOrderTrackingId());
                    
                    // Create payment status entry
                    paymentStatusService.createPaymentStatus(
                        payment,
                        paymentResponse.getMerchantReference(),
                        paymentResponse.getRedirectUrl()
                    );

                    // Return the full Pesapal response
                    return objectMapper.writeValueAsString(paymentResponse);
                } else {
                    logger.error("Payment failed: {}", paymentResponse.getError());
                    return objectMapper.writeValueAsString(paymentResponse);
                }
            } catch (Exception e) {
                logger.error("Failed to parse payment response: {}", e.getMessage());
                PaymentResponse errorResponse = new PaymentResponse();
                errorResponse.setStatus("500");
                
                PaymentResponse.Error error = new PaymentResponse.Error();
                error.setErrorType("SYSTEM_ERROR");
                error.setCode("500");
                error.setMessage(e.getMessage());
                errorResponse.setError(error);
                
                return objectMapper.writeValueAsString(errorResponse);
            }
        } catch (IOException e) {
            logger.error("Error processing payment request", e);
            throw e;
        }
    }

    /**
     * Ensure we have a valid access token
     * @throws IOException if unable to obtain token
     */
    private void ensureValidAccessToken() throws IOException {
        if (accessToken == null || accessToken.isEmpty()) {
            logger.info("No access token found, attempting to authenticate...............................................");
            authenticatePesapal();
        } else {
            logger.info("Using existing access token");
        }
    }


    private void validatePaymentRequest(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment request cannot be null");
        }

        if (payment.getCurrency() == null || payment.getCurrency().isEmpty()) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (payment.getDescription() == null || payment.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description is required");
        }

        if (payment.getCallbackUrl() == null || payment.getCallbackUrl().isEmpty()) {
            throw new IllegalArgumentException("Callback URL is required");
        }

        if (payment.getBillingAddress() == null) {
            throw new IllegalArgumentException("Billing address is required");
        }

        BillingAddress billingAddress = payment.getBillingAddress();
        if (billingAddress.getEmailAddress() == null || billingAddress.getEmailAddress().isEmpty()) {
            throw new IllegalArgumentException("Billing address email is required");
        }

        if (billingAddress.getPhoneNumber() == null || billingAddress.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Billing address phone number is required");
        }

        if (billingAddress.getCountryCode() == null || billingAddress.getCountryCode().isEmpty()) {
            throw new IllegalArgumentException("Billing address country code is required");
        }

        if (billingAddress.getFirstName() == null || billingAddress.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("Billing address first name is required");
        }

        // Make last name, line1, line2, city, state, postal_code, zip_code optional
        if (billingAddress.getLastName() == null) {
            billingAddress.setLastName("");
        }

        if (billingAddress.getLine1() == null) {
            billingAddress.setLine1("");
        }

        if (billingAddress.getLine2() == null) {
            billingAddress.setLine2("");
        }

        if (billingAddress.getCity() == null) {
            billingAddress.setCity("");
        }

        if (billingAddress.getState() == null) {
            billingAddress.setState("");
        }

        if (billingAddress.getPostalCode() == null) {
            billingAddress.setPostalCode("");
        }

        if (billingAddress.getZipCode() == null) {
            billingAddress.setZipCode("");
        }
    }
}