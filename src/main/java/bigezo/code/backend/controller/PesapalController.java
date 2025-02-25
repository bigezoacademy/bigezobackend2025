package bigezo.code.backend.controller;

import bigezo.code.backend.PaymentTokenResponse;
import bigezo.code.backend.service.PesapalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pesapal")
public class PesapalController {

    @Value("${api.key}")
    private String apiKey;  // Your API key for validation

    private final PesapalService pesapalService;

    @Autowired
    public PesapalController(PesapalService pesapalService) {
        this.pesapalService = pesapalService;
    }

    @PostMapping("/request-token")
    public ResponseEntity<?> requestToken() {
        try {
            // Call the service to get the token
            String jwtToken = pesapalService.getJwtToken();
            System.out.println("FETCHED ONLY TOKEN-----------------------"+jwtToken);

            // Get current timestamp in ISO 8601 format (UTC)
            String currentTimestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now().atZone(ZoneOffset.UTC));

            // Create the response object
            PaymentTokenResponse response = new PaymentTokenResponse(jwtToken, currentTimestamp, null, "200", "Request processed successfully");

            // Convert the response object to JSON format
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(response);

            // Print JSON output to the terminal
            System.out.println("Response JSON: " + jsonResponse);

            // Return the JSON response
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            System.err.println("Error requesting token: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to request token: " + e.getMessage());
        }
    }

    @GetMapping("/transaction-status")
    public ResponseEntity<?> getTransactionStatus(@RequestParam String orderTrackingId) {
        try {
            String response = pesapalService.getTransactionStatus(orderTrackingId);

            // Print the response to the console
            System.out.println("Pesapal Transaction Status Response: " + response);

            // Return a JSON response
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("orderTrackingId", orderTrackingId);
            responseBody.put("status", response);

            return ResponseEntity.ok().body(responseBody);
        } catch (Exception e) {
            System.out.println("Error fetching transaction status:----- " + e.getMessage());

            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch transaction status");
            errorResponse.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
