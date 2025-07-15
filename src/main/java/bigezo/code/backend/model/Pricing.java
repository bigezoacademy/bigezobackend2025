package bigezo.code.backend.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pricing")
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tier = "PAID";

    @Column(nullable = false)
    private BigDecimal costPerStudent;

    public Pricing() {
    }

    public Pricing(String tier, BigDecimal costPerStudent) {
        this.tier = tier;
        this.costPerStudent = costPerStudent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public BigDecimal getCostPerStudent() {
        return costPerStudent;
    }

    public void setCostPerStudent(BigDecimal costPerStudent) {
        this.costPerStudent = costPerStudent;
    }
}
