package bigezo.code.backend.model;

import bigezo.code.backend.SchoolAdmin;
import jakarta.persistence.*;

@Entity
@Table(name = "requirements")
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String item;
    private String description;
    private Double unitCost;
    private String level; // Use 'level' if 'class' is reserved
    private Integer term; // Changed to Integer to represent an integer value
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "school_admin_id", referencedColumnName = "id", nullable = false)
    private SchoolAdmin schoolAdmin; // Reference to SchoolAdmin entity

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Integer getTerm() {  // Corrected to return Integer instead of String
        return term;
    }

    public void setTerm(Integer term) {  // Corrected to accept Integer as input
        this.term = term;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public SchoolAdmin getSchoolAdmin() {
        return schoolAdmin;
    }

    public void setSchoolAdmin(SchoolAdmin schoolAdmin) {
        this.schoolAdmin = schoolAdmin;
    }
}
