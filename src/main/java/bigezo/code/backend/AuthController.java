package bigezo.code.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SchoolAdminRepository schoolAdminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Helper method to create a consistent response body
    private Map<String, Object> createResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            logger.info("Attempting to login user: {}", username);

            User user = userRepository.findByUsername(username);

            // Check if user exists in the database
            if (user == null) {
                logger.warn("User not found:"+username+"---", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createResponse(false, "Invalid credentials", null));
            }

            // Check if password matches the hashed password
            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Password matches for user: {}", username);
                String token = jwtUtil.generateToken(user.getUsername());
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("token", token);
                responseData.put("role", user.getRoles()); // Include role in the response
                responseData.put("userId",user.getId());
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createResponse(true, "Login successful", responseData));
            }
            else {
                logger.warn("Invalid password for user: {}", username);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createResponse(false, "Invalid credentials", null));
            }
        } catch (Exception e) {
            logger.error("Unexpected error during login for user: {}", loginRequest.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createResponse(false, "An error occurred during login", null));
        }
    }

    @PostMapping("/registerschool")
    public ResponseEntity<Map<String, Object>> registerSchoolAdmin(@RequestBody SchoolAdmin schoolAdmin) {
        try {
            // Check if adminUsername already exists in SchoolAdmin
            if (schoolAdminRepository.existsByAdminUsername(schoolAdmin.getAdminUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(createResponse(false, "Admin username already exists", null));
            }

            // Hash the password
            String hashedPassword = passwordEncoder.encode(schoolAdmin.getAdminPassword());
            schoolAdmin.setAdminPassword(hashedPassword);

            // Set default role
            schoolAdmin.setRole("admin");

            // Save the school admin
            schoolAdminRepository.save(schoolAdmin);

            // Create a corresponding User for authentication
            User user = new User(schoolAdmin.getAdminUsername(), hashedPassword, "ROLE_ADMIN");
            userRepository.save(user);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createResponse(true, "School admin registered successfully!", null));
        } catch (Exception e) {
            logger.error("Unexpected error during school admin registration", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createResponse(false, "An error occurred during registration", null));
        }
    }
}
