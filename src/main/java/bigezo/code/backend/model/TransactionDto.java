package bigezo.code.backend.model;

import java.time.LocalDateTime;

public class TransactionDto {

    private Long studentId;
    private String studentName;
    private double amount;
    private String orderTrackingId;
    private String reason;
    private String description;
    private String statusCode;
    private String status;
    private LocalDateTime time;
    private int level;
    private String term;
    private int year;
    private Long schoolAdminId;

    // Getters and Setters

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getOrderTrackingId() {
        return orderTrackingId;
    }

    public void setOrderTrackingId(String orderTrackingId) {
        this.orderTrackingId = orderTrackingId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Long getSchoolAdminId() {
        return schoolAdminId;
    }

    public void setSchoolAdminId(Long schoolAdminId) {
        this.schoolAdminId = schoolAdminId;
    }
}
