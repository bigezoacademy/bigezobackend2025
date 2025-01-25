package bigezo.code.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;

@Service
public class PesapalService {

    private static final String PESAPAL_TOKEN_URL = "https://pay.pesapal.com/v3/api/Auth/RequestToken";

    @Value("${pesapal.consumer.key}")
    private String consumerKey;

    @Value("${pesapal.consumer.secret}")
    private String consumerSecret;

    public String getJwtToken() throws Exception {
        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // Prepare body as JSON
        String requestBody = "{\r\n" +
                "  \"consumer_key\": \"" + consumerKey + "\",\r\n" +
                "  \"consumer_secret\": \"" + consumerSecret + "\"\r\n" +
                "}";

        // Log the request payload (for debugging purposes)
        System.out.println("Request Payload: " + requestBody);

        // Use RestTemplate to make the HTTP request
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                PESAPAL_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        // Log the response from Pesapal
        System.out.println("Response from Pesapal: " + response.getBody());

        // Check if the response is successful
        if (response.getStatusCode() == HttpStatus.OK) {
            // Parse the response body
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response.getBody());

            // Check for errors in the response
            if (responseJson.has("error") && !responseJson.get("error").isNull()) {
                String errorMessage = responseJson.get("message").asText();
                String errorCode = responseJson.get("status").asText();
                throw new Exception("Error: " + errorMessage + " | Code: " + errorCode);
            }

            // Return the token if successful
            return responseJson.get("token").asText();
        } else {
            // Handle HTTP errors
            throw new Exception("Failed to retrieve JWT token. HTTP Status: " + response.getStatusCode());
        }
    }
}
