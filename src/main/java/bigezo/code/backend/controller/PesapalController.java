package bigezo.code.backend.controller;

import bigezo.code.backend.PaymentTokenResponse;
import bigezo.code.backend.service.PesapalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
    public ResponseEntity<?> requestToken(@RequestHeader(value = "X-API-KEY", required = false) String providedApiKey) {
        if (providedApiKey == null || !providedApiKey.equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key");
        }

        try {
            // Call the service to get the token
            String jwtToken = pesapalService.getJwtToken();

            // Get current timestamp in ISO 8601 format (UTC)
            String currentTimestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now().atZone(ZoneOffset.UTC));

            // Create a JSON response with the payment token and dynamic timestamp
            return ResponseEntity.ok().body(new PaymentTokenResponse(jwtToken, currentTimestamp, null, "200", "Request processed successfully"));
        } catch (Exception e) {
            System.err.println("Error requesting token: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to request token: " + e.getMessage());
        }
    }

}



