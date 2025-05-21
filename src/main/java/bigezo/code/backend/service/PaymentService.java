package bigezo.code.backend.service;

import bigezo.code.backend.dto.PaymentRequestDTO;
import bigezo.code.backend.model.*;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
    private static final int TOKEN_EXPIRY_MINUTES = 5;

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

    private synchronized void authenticatePesapal() throws IOException {
        logger.info("Attempting to authenticate with Pesapal...");
        
        if (StringUtils.hasText(consumerKey) && StringUtils.hasText(consumerSecret)) {
            // Check if we need to refresh token
            if (accessToken.isEmpty() || 
                lastAuthTime == null || 
                LocalDateTime.now().minusMinutes(TOKEN_EXPIRY_MINUTES).isAfter(lastAuthTime)) {
                
                logger.info("Token expired or not set, refreshing...");
                String authJson = String.format("{\"consumer_key\":\"%s\",\"consumer_secret\":\"%s\"}", consumerKey, consumerSecret);

                RequestBody body = RequestBody.create(authJson, MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url(PESAPAL_AUTH_URL)
                        .post(body)
                        .build();

                int retryCount = 0;
                while (retryCount < MAX_AUTH_RETRIES) {
                    try (Response response = client.newCall(request).execute()) {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            logger.info("Successfully obtained Pesapal access token response: {}", responseBody);
                            
                            // Parse the token response
                            try {
                                // The response is a JSON object with token and other fields
                                if (responseBody != null && !responseBody.isEmpty()) {
                                    // Extract token directly from the JSON string
                                    String token = responseBody.substring(responseBody.indexOf("token\":") + 7);
                                    token = token.substring(0, token.indexOf(","));
                                    token = token.replace("\"", "");
                                    
                                    if (token != null && !token.isEmpty()) {
                                        accessToken = token;
                                        lastAuthTime = LocalDateTime.now();
                                        return;
                                    }
                                }
                                throw new IOException("No token found in response: " + responseBody);
                            } catch (Exception e) {
                                throw new IOException("Failed to parse token response: " + responseBody, e);
                            }
                        } else {
                            String errorBody = response.body().string();
                            logger.error("Authentication attempt {} failed: {} - {}", retryCount + 1, response.code(), errorBody);
                            retryCount++;
                            
                            if (retryCount >= MAX_AUTH_RETRIES) {
                                throw new IOException("Failed to authenticate after " + MAX_AUTH_RETRIES + " attempts: " + errorBody);
                            }
                            
                            // Wait before retrying
                            try {
                                Thread.sleep(1000 * (retryCount + 1)); // Exponential backoff
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                                throw new IOException("Interrupted while waiting for retry", ex);
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error during authentication attempt {}: {}", retryCount + 1, e.getMessage());
                        retryCount++;
                        
                        if (retryCount >= MAX_AUTH_RETRIES) {
                            throw new IOException("Failed to authenticate after " + MAX_AUTH_RETRIES + " attempts: " + e.getMessage());
                        }
                        
                        // Wait before retrying
                        try {
                            Thread.sleep(1000 * (retryCount + 1)); // Exponential backoff
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                            throw new IOException("Interrupted while waiting for retry", ex);
                        }
                    }
                }
            }
        } else {
            logger.error("Missing Pesapal credentials. consumerKey: {} consumerSecret: {}",
                    StringUtils.hasText(consumerKey), StringUtils.hasText(consumerSecret));
            throw new IOException("Missing Pesapal credentials");
        }
    }

    /**
     * Make a payment request to Pesapal
     * @param requestDTO The payment request details
     * @return The saved payment object
     * @throws IOException if payment processing fails
     */
    public String makePayment(PaymentRequestDTO requestDTO) throws IOException {
        logger.info("Processing payment request: {}", requestDTO);

        // First, ensure we have a valid token
        try {
            authenticatePesapal();
        } catch (IOException e) {
            logger.error("Failed to authenticate with Pesapal: {}", e.getMessage());
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setStatus("500");
            errorResponse.setError(new PaymentResponse.Error());
            errorResponse.getError().setErrorType("AUTHENTICATION_ERROR");
            errorResponse.getError().setCode("500");
            errorResponse.getError().setMessage("Failed to obtain Pesapal access token: " + e.getMessage());
            return objectMapper.writeValueAsString(errorResponse);
        }

        // Convert DTO to entity
        Payment payment = new Payment();
        payment.setPesapalId(requestDTO.getId());
        payment.setCurrency(requestDTO.getCurrency());
        payment.setAmount(requestDTO.getAmount());
        payment.setDescription(requestDTO.getDescription());
        payment.setCallbackUrl(requestDTO.getCallbackUrl());
        payment.setRedirectMode(requestDTO.getRedirectMode());
        payment.setNotificationId(requestDTO.getNotificationId());
        payment.setBranch(requestDTO.getBranch());

        // Create and set billing address
        if (requestDTO.getBillingAddress() != null) {
            BillingAddress billingAddress = new BillingAddress();
            billingAddress.setEmailAddress(requestDTO.getBillingAddress().getEmailAddress());
            billingAddress.setPhoneNumber(requestDTO.getBillingAddress().getPhoneNumber());
            billingAddress.setCountryCode(requestDTO.getBillingAddress().getCountryCode());
            billingAddress.setFirstName(requestDTO.getBillingAddress().getFirstName());
            billingAddress.setMiddleName(requestDTO.getBillingAddress().getMiddleName());

            payment.setBillingAddress(billingAddress);
        }

        try {
            // Make payment request
            String requestBody = objectMapper.writeValueAsString(payment);
            RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(PESAPAL_PAYMENT_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .build();

            Response initialResponse = client.newCall(request).execute();
            Response finalResponse;
            
            if (!initialResponse.isSuccessful()) {
                if (initialResponse.code() == 401) {
                    // Token expired, try to refresh and retry
                    logger.info("Token expired, attempting to refresh...");
                    authenticatePesapal();
                    
                    // Retry the payment request with new token
                    request = new Request.Builder()
                            .url(PESAPAL_PAYMENT_URL)
                            .post(body)
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .build();
                    
                    try (Response retryResponse = client.newCall(request).execute()) {
                        if (!retryResponse.isSuccessful()) {
                            throw new IOException("Payment request failed after token refresh: " + retryResponse.code() + " - " + retryResponse.body().string());
                        }
                        finalResponse = retryResponse;
                    }
                } else {
                    throw new IOException("Payment request failed: " + initialResponse.code() + " - " + initialResponse.body().string());
                }
            } else {
                finalResponse = initialResponse;
            }
            
            try (Response response = finalResponse) {

                String responseBody = response.body().string();
                logger.info("Payment response received: {}", responseBody);

                // Parse response
                PaymentResponse paymentResponse = objectMapper.readValue(responseBody, PaymentResponse.class);

                logger.info("Payment response received: {}", paymentResponse);

                // Check if the response is successful
                if (paymentResponse.getStatus().equals("200") && paymentResponse.getError() == null) {
                    logger.info("Payment successfully processed");

                    // Create a Payment object from the response
                    Payment paymentResponseEntity = new Payment();
                    paymentResponseEntity.setPesapalId(paymentResponse.getMerchantReference());
                    paymentResponseEntity.setNotificationId(paymentResponse.getOrderTrackingId());

                    // Create payment status entry
                    paymentStatusService.createPaymentStatus(
                        paymentResponseEntity,
                        paymentResponse.getMerchantReference(),
                        paymentResponse.getRedirectUrl()
                    );

                    // Return the full Pesapal response
                    return objectMapper.writeValueAsString(paymentResponse);
                } else {
                    logger.error("Payment failed: {}", paymentResponse.getError());
                    return objectMapper.writeValueAsString(paymentResponse);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to parse payment response: {}", e.getMessage());
            PaymentResponse errorResponse = new PaymentResponse();
            errorResponse.setStatus("500");
            errorResponse.setError(new PaymentResponse.Error());
            errorResponse.getError().setErrorType("SYSTEM_ERROR");
            errorResponse.getError().setCode("500");
            errorResponse.getError().setMessage(e.getMessage());
            return objectMapper.writeValueAsString(errorResponse);
        }
    }

    /**
     * Ensure we have a valid access token
     * @throws IOException if unable to obtain token
     */
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