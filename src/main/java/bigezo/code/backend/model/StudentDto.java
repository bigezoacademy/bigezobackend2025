package bigezo.code.backend.model;

import java.time.LocalDate;

public class StudentDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String level;
    private String club;
    private String healthStatus;
    private String studentNumber;
    private LocalDate birthDate;
    private String residence;
    private String mother;
    private String father;
    private String phone;
    private String email;
    private String password;
    private String enrollmentStatus;
    private Integer year;
    private Long schoolAdminId; // Add this field for the reference to SchoolAdmin

    // Constructor, Getters, and Setters
    public StudentDto(Long id, String firstName, String lastName, String level, String club,
                      String healthStatus, String studentNumber, LocalDate birthDate, String residence,
                      String mother, String father, String phone, String email, String password,
                      String enrollmentStatus, Integer year, Long schoolAdminId) {
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
        this.password = password;
        this.enrollmentStatus = enrollmentStatus;
        this.year = year;
        this.schoolAdminId = schoolAdminId;
    }

    // Getters and Setters for all fields
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

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(String enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getSchoolAdminId() {
        return schoolAdminId;
    }

    public void setSchoolAdminId(Long schoolAdminId) {
        this.schoolAdminId = schoolAdminId;
    }
}
