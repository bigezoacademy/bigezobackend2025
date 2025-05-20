/* <<<<<<<<<<<<<<  ✨ Windsurf Command 🌟 >>>>>>>>>>>>>>>> */
package bigezo.code.backend.service;

import bigezo.code.backend.model.TransactionStatusResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PesapalService {

    private static final String PESAPAL_TOKEN_URL = "https://pay.pesapal.com/v3/api/Auth/RequestToken";
    private static final String PESAPAL_ORDER_URL = "https://pay.pesapal.com/v3/api/Transactions/SubmitOrderRequest";
    private static final String PESAPAL_STATUS_URL = "https://pay.pesapal.com/v3/api/Transactions/GetTransactionStatus?orderTrackingId=";

    @Value("${pesapal.consumer.key}")
    private String consumerKey;

    @Value("${pesapal.consumer.secret}")
    private String consumerSecret;

    public String getJwtToken() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String requestBody = "{ \"consumer_key\": \"" + consumerKey + "\", \"consumer_secret\": \"" + consumerSecret + "\" }";

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(PESAPAL_TOKEN_URL, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.getBody());

            // Check if there is an error and throw an exception if so
            if (responseJson.has("error") && !responseJson.get("error").isNull()) {
                throw new Exception("Error: " + responseJson.get("message").asText());
            }

            // Return only the token string from the response
            if (responseJson.has("token")) {
                String token = responseJson.get("token").asText();
                System.out.println("Fetched Token: " + token);  // Print the token for debugging
                return token;  // Return only the token string
            } else {
                throw new Exception("Token not found in the response.");
            }
        } else {
            throw new Exception("Failed to retrieve JWT token. HTTP Status: " + response.getStatusCode());
        }
    }


    /**
     * Submits a new payment request to Pesapal.
     * @param amount - The payment amount.
     * @param description - Payment description.
     * @param currency - Currency (e.g., "KES").
     * @param callbackUrl - URL where Pesapal will send updates.
     * @return Order Tracking ID.
     */
    public String submitPayment(double amount, String description, String currency, String callbackUrl) throws Exception {
        String token = getJwtToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Construct the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", amount);
        requestBody.put("currency", currency);
        requestBody.put("description", description);
        requestBody.put("callback_url", callbackUrl);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(PESAPAL_ORDER_URL, HttpMethod.POST, requestEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            return responseJson.get("orderTrackingId").asText();
        } else {
            throw new Exception("Failed to submit payment. HTTP Status: " + response.getStatusCode());
        }
    }

    /**
     * Fetches the payment status from Pesapal using the Order Tracking ID.
     * @param orderTrackingId - Unique identifier returned when submitting a payment.
     * @return Payment status (e.g., "PENDING", "COMPLETED", "FAILED").
     */

    public TransactionStatusResponse getTransactionStatus(String orderTrackingId) throws Exception {
        String jwtToken = getJwtToken(); // Fetch JWT Token
        System.out.println("FETCHED STATUS TOKEN-----------------------" + jwtToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String url = PESAPAL_STATUS_URL + orderTrackingId;
        String PESAPAL_TRANSACTION_STATUS_URL = "https://pay.pesapal.com/v3/api/Transactions/GetTransactionStatus?orderTrackingId=";

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);

        System.out.println("Transaction Status Response: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON response to the DTO
            return objectMapper.readValue(response.getBody(), TransactionStatusResponse.class);
        } else {
            throw new Exception("Failed to fetch transaction status. HTTP Status: " + response.getStatusCode());
        }
    }



}
/* <<<<<<<<<<  f153854a-5377-4823-ba28-d93bbf524ba1  >>>>>>>>>>> */