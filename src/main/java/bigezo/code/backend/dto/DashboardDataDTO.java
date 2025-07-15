package bigezo.code.backend.dto;

import java.math.BigDecimal;

public class DashboardDataDTO {
    private BigDecimal requirementsRevenue;
    private BigDecimal alumniContributions;
    private Long studentsRegistered;
    private Long schoolFeesDefaulters;
    private ChartDataDTO studentPerformanceData;
    private GenderChartDataDTO genderEnrollmentData;

    public DashboardDataDTO() {
    }

    public DashboardDataDTO(BigDecimal requirementsRevenue, BigDecimal alumniContributions, Long studentsRegistered, Long schoolFeesDefaulters, ChartDataDTO studentPerformanceData, GenderChartDataDTO genderEnrollmentData) {
        this.requirementsRevenue = requirementsRevenue;
        this.alumniContributions = alumniContributions;
        this.studentsRegistered = studentsRegistered;
        this.schoolFeesDefaulters = schoolFeesDefaulters;
        this.studentPerformanceData = studentPerformanceData;
        this.genderEnrollmentData = genderEnrollmentData;
    }

    public BigDecimal getRequirementsRevenue() {
        return requirementsRevenue;
    }

    public void setRequirementsRevenue(BigDecimal requirementsRevenue) {
        this.requirementsRevenue = requirementsRevenue;
    }

    public BigDecimal getAlumniContributions() {
        return alumniContributions;
    }

    public void setAlumniContributions(BigDecimal alumniContributions) {
        this.alumniContributions = alumniContributions;
    }

    public Long getStudentsRegistered() {
        return studentsRegistered;
    }

    public void setStudentsRegistered(Long studentsRegistered) {
        this.studentsRegistered = studentsRegistered;
    }

    public Long getSchoolFeesDefaulters() {
        return schoolFeesDefaulters;
    }

    public void setSchoolFeesDefaulters(Long schoolFeesDefaulters) {
        this.schoolFeesDefaulters = schoolFeesDefaulters;
    }

    public ChartDataDTO getStudentPerformanceData() {
        return studentPerformanceData;
    }

    public void setStudentPerformanceData(ChartDataDTO studentPerformanceData) {
        this.studentPerformanceData = studentPerformanceData;
    }

    public GenderChartDataDTO getGenderEnrollmentData() {
        return genderEnrollmentData;
    }

    public void setGenderEnrollmentData(GenderChartDataDTO genderEnrollmentData) {
        this.genderEnrollmentData = genderEnrollmentData;
    }
}
