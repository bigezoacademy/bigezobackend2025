package bigezo.code.backend.model;

import bigezo.code.backend.SchoolAdmin;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "school_fees_setting")
public class SchoolFeesSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reason;
    private String level;
    private String term;
    private String year;
    private Double total;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "school_admin_id", referencedColumnName = "id", nullable = false)
    private SchoolAdmin schoolAdmin;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }

    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public Double getTotal() {
        return total;
    }
    public void setTotal(Double total) {
        this.total = total;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public SchoolAdmin getSchoolAdmin() {
        return schoolAdmin;
    }
    public void setSchoolAdmin(SchoolAdmin schoolAdmin) {
        this.schoolAdmin = schoolAdmin;
    }
}