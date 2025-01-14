package bigezo.code.backend.model;

import bigezo.code.backend.SchoolAdmin;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "school_admin_id", referencedColumnName = "id", nullable = false)
    private SchoolAdmin schoolAdmin; // Reference to SchoolAdmin entity

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getClub() { return club; }
    public void setClub(String club) { this.club = club; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getResidence() { return residence; }
    public void setResidence(String residence) { this.residence = residence; }

    public String getMother() { return mother; }
    public void setMother(String mother) { this.mother = mother; }

    public String getFather() { return father; }
    public void setFather(String father) { this.father = father; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public SchoolAdmin getSchoolAdmin() {
        return schoolAdmin;
    }

    public void setSchoolAdmin(SchoolAdmin schoolAdmin) {
        this.schoolAdmin = schoolAdmin;
    }
}
