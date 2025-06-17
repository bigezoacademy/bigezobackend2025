package bigezo.code.backend.model;

import bigezo.code.backend.dto.SchoolAdminDto;

public class RequirementDto {
    private Long id;
    private String item;
    private String description;
    private Double unitCost;
    private String level;
    private Integer term;  // This field should be Integer
    private Integer year;
    private Integer quantity; // New field added for quantity
    private SchoolAdminDto schoolAdmin;

    // Constructor
    public RequirementDto(Long id, String item, String description, Double unitCost, String level,
                          Integer term, Integer year, Integer quantity, SchoolAdminDto schoolAdmin) {
        this.id = id;
        this.item = item;
        this.description = description;
        this.unitCost = unitCost;
        this.level = level;
        this.term = term;
        this.year = year;
        this.quantity = quantity;  // Initialize quantity
        this.schoolAdmin = schoolAdmin;
    }

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

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getQuantity() {  // Getter for quantity
        return quantity;
    }

    public void setQuantity(Integer quantity) {  // Setter for quantity
        this.quantity = quantity;
    }

    public SchoolAdminDto getSchoolAdmin() {
        return schoolAdmin;
    }

    public void setSchoolAdmin(SchoolAdminDto schoolAdmin) {
        this.schoolAdmin = schoolAdmin;
    }
}
