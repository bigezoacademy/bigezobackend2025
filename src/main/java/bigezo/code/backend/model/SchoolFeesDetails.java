package bigezo.code.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "school_fees_details")
public class SchoolFeesDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fees_id", referencedColumnName = "id", nullable = false)
    private SchoolFeesSetting schoolFeesSetting;

    private String item;
    private String description;
    private Double amount;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public SchoolFeesSetting getSchoolFeesSetting() {
        return schoolFeesSetting;
    }
    public void setSchoolFeesSetting(SchoolFeesSetting schoolFeesSetting) {
        this.schoolFeesSetting = schoolFeesSetting;
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

    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
