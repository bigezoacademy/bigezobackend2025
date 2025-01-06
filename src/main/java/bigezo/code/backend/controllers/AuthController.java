package bigezo.code.backend.controllers;

import bigezo.code.backend.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.server.ResponseStatusException;

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


    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            logger.info("Attempting to login user: {}", username);

            User user = userRepository.findByUsername(username);

            // Check if user exists in the database
            if (user == null) {
                logger.warn("User not found: {}", username);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }

            logger.info("User found, checking password for: {}", username);

            // Check if password matches the hashed password
            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Password matches for user: {}", username);
                return jwtUtil.generateToken(user.getUsername());
            } else {
                logger.warn("Invalid password for user: {}", username);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
            }
        } catch (ResponseStatusException e) {
            logger.error("Login failed due to invalid credentials for user: {}", loginRequest.getUsername(), e);
            throw e; // Re-throw the exception after logging
        } catch (Exception e) {
            logger.error("Unexpected error during login for user: {}", loginRequest.getUsername(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred during login", e);
        }
    }


    @PostMapping("/registerschool")
    public String registerSchoolAdmin(@RequestBody SchoolAdmin schoolAdmin) {
        // Check if adminUsername already exists in SchoolAdmin
        if (schoolAdminRepository.existsByAdminUsername(schoolAdmin.getAdminUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin username already exists");
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

        return "School admin registered successfully!";
    }

}
