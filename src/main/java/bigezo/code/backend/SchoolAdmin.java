package bigezo.code.backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Random;

@Entity
public class SchoolAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;
    private String adminUsername;
    private String adminPassword;
    private String adminPhone;
    private String adminEmail;
    private String district;
    private String role; // Added role field
    private String uniqueCode; // New field for unique code

    // Default constructor
    public SchoolAdmin() {
        this.role = "admin"; // Set default role to "admin"
    }

    // Constructor with schoolName to automatically generate uniqueCode
    public SchoolAdmin(String schoolName, String adminUsername, String adminPassword, String adminPhone,
                       String adminEmail, String district) {
        this.schoolName = schoolName;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminPhone = adminPhone;
        this.adminEmail = adminEmail;
        this.district = district;
        this.role = "admin";
        this.uniqueCode = generateUniqueCode(schoolName); // Generate unique code
    }

    // Method to generate uniqueCode from the first 3 letters of schoolName, replacing spaces with random letters
    private String generateUniqueCode(String schoolName) {
        if (schoolName != null && schoolName.length() >= 3) {
            // Replace spaces with random letters
            StringBuilder sanitizedSchoolName = new StringBuilder();
            Random random = new Random();
            for (char c : schoolName.toCharArray()) {
                if (c == ' ') {
                    sanitizedSchoolName.append((char) ('A' + random.nextInt(26))); // Replace space with a random letter
                } else {
                    sanitizedSchoolName.append(c);
                }
            }
            // Return the first 3 letters of the sanitized school name, converted to uppercase
            return sanitizedSchoolName.substring(0, 3).toLowerCase();
        } else {
            return generateRandomCode(); // Generate random 5-character code if schoolName is invalid
        }
    }

    // Method to generate a random 5-character alphanumeric code
    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder randomCode = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int randInt = random.nextInt(36); // Generate a number between 0 and 35
            if (randInt < 10) {
                randomCode.append(randInt); // Append a digit
            } else {
                randomCode.append((char) ('A' + randInt - 10)); // Append a letter (A-Z)
            }
        }
        return randomCode.toString();
    }

    // Getters and Setters (include role and uniqueCode)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
        this.uniqueCode = generateUniqueCode(schoolName); // Re-generate uniqueCode if schoolName changes
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }
}
