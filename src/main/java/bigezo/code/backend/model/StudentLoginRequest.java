package bigezo.code.backend.model;


public class StudentLoginRequest {
    private String studentNumber;
    private String password;

    // Default constructor
    public StudentLoginRequest() {}

    // Constructor with fields
    public StudentLoginRequest(String studentNumber, String password) {
        this.studentNumber = studentNumber;
        this.password = password;
    }

    // Getters and setters
    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

