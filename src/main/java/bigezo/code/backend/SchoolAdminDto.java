package bigezo.code.backend;

public class SchoolAdminDto {
    private Long id;
    private String schoolName;
    private String district;

    // Constructor
    public SchoolAdminDto(Long id, String schoolName, String district) {
        this.id = id;
        this.schoolName = schoolName;
        this.district = district;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}

