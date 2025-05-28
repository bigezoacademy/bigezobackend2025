package bigezo.code.backend.model;

import java.time.LocalDate;

public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String level;
    private String gender;
    private String club;
    private String healthStatus;
    private String studentNumber;
    private LocalDate birthDate;
    private String residence;
    private String mother;
    private String father;
    private String phone;
    private String email;
    private String enrollmentStatus;
    private Integer year;
    private Long schoolAdminId;

    // File references
    private String profilePictureUrl;
    private String studentVideoUrl;

    // Store up to 10 image URLs as individual fields
    private String image1Url;
    private String image2Url;
    private String image3Url;
    private String image4Url;
    private String image5Url;
    private String image6Url;
    private String image7Url;
    private String image8Url;
    private String image9Url;
    private String image10Url;

    public StudentDto(Long id, String firstName, String lastName, String level, String club, String healthStatus,
                      String studentNumber, LocalDate birthDate, String residence, String mother, String father,
                      String phone, String email, String enrollmentStatus, int year, String gender, Long schoolAdminId,
                      String profilePictureUrl, String studentVideoUrl,
                      String image1Url, String image2Url, String image3Url, String image4Url,
                      String image5Url, String image6Url, String image7Url, String image8Url,
                      String image9Url, String image10Url) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
        this.club = club;
        this.healthStatus = healthStatus;
        this.studentNumber = studentNumber;
        this.birthDate = birthDate;
        this.residence = residence;
        this.mother = mother;
        this.father = father;
        this.phone = phone;
        this.email = email;
        this.enrollmentStatus = enrollmentStatus;
        this.year = year;
        this.gender = gender;
        this.schoolAdminId = schoolAdminId;
        this.profilePictureUrl = profilePictureUrl;
        this.studentVideoUrl = studentVideoUrl;
        this.image1Url = image1Url;
        this.image2Url = image2Url;
        this.image3Url = image3Url;
        this.image4Url = image4Url;
        this.image5Url = image5Url;
        this.image6Url = image6Url;
        this.image7Url = image7Url;
        this.image8Url = image8Url;
        this.image9Url = image9Url;
        this.image10Url = image10Url;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getHealthStatus() {
        return healthStatus;
    }
    public String getMother() { return mother; }
    public void setMother(String mother) { this.mother = mother; }

    public String getFather() { return father; }
    public void setFather(String father) { this.father = father; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getGender() { return gender; } // Add getter for gender
    public void setGender(String gender) { this.gender = gender; } // Add setter for gender

    // Getters and Setters for file references
    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }
    public String getStudentVideoUrl() { return studentVideoUrl; }
    public void setStudentVideoUrl(String studentVideoUrl) { this.studentVideoUrl = studentVideoUrl; }

    // Getters and Setters for image URLs
    public String getImage1Url() { return image1Url; }
    public void setImage1Url(String image1Url) { this.image1Url = image1Url; }
    public String getImage2Url() { return image2Url; }
    public void setImage2Url(String image2Url) { this.image2Url = image2Url; }
    public String getImage3Url() { return image3Url; }
    public void setImage3Url(String image3Url) { this.image3Url = image3Url; }
    public String getImage4Url() { return image4Url; }
    public void setImage4Url(String image4Url) { this.image4Url = image4Url; }
    public String getImage5Url() { return image5Url; }
    public void setImage5Url(String image5Url) { this.image5Url = image5Url; }
    public String getImage6Url() { return image6Url; }
    public void setImage6Url(String image6Url) { this.image6Url = image6Url; }
    public String getImage7Url() { return image7Url; }
    public void setImage7Url(String image7Url) { this.image7Url = image7Url; }
    public String getImage8Url() { return image8Url; }
    public void setImage8Url(String image8Url) { this.image8Url = image8Url; }
    public String getImage9Url() { return image9Url; }
    public void setImage9Url(String image9Url) { this.image9Url = image9Url; }
    public String getImage10Url() { return image10Url; }
    public void setImage10Url(String image10Url) { this.image10Url = image10Url; }

    public Long getSchoolAdminId() { return schoolAdminId; }
    public void setSchoolAdminId(Long schoolAdminId) { this.schoolAdminId = schoolAdminId; }
}