package bigezo.code.backend.service;

import bigezo.code.backend.dto.ChartDataDTO;
import bigezo.code.backend.dto.DashboardDataDTO;
import bigezo.code.backend.dto.GenderChartDataDTO;
import bigezo.code.backend.repository.StudentRepository;
import bigezo.code.backend.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.time.Year;

@Service
public class DashboardService {

    private final StudentRepository studentRepository;
    private final TransactionRepository transactionRepository;

    public DashboardService(StudentRepository studentRepository, TransactionRepository transactionRepository) {
        this.studentRepository = studentRepository;
        this.transactionRepository = transactionRepository;
    }

    public DashboardDataDTO getDashboardData(Long schoolAdminId) {
        DashboardDataDTO dashboardData = new DashboardDataDTO();

        // Students Registered
        Long studentsRegistered = studentRepository.countBySchoolAdminId(schoolAdminId);
        dashboardData.setStudentsRegistered(studentsRegistered);

        // School Fees Defaulters (using pending transactions as a proxy)
        Long schoolFeesDefaulters = transactionRepository.countPendingTransactionsBySchoolAdminId(schoolAdminId);
        dashboardData.setSchoolFeesDefaulters(schoolFeesDefaulters);

        // Requirements Revenue
        Double reqRevenue = transactionRepository.sumAmountByReasonAndSchoolAdminId(schoolAdminId, "Requirements");
        dashboardData.setRequirementsRevenue(reqRevenue != null ? BigDecimal.valueOf(reqRevenue) : BigDecimal.ZERO);

        // Alumni Contributions
        Double alumniCont = transactionRepository.sumAmountByReasonAndSchoolAdminId(schoolAdminId, "Alumni");
        dashboardData.setAlumniContributions(alumniCont != null ? BigDecimal.valueOf(alumniCont) : BigDecimal.ZERO);

        // Student Performance Data (Enrollment over years)
        List<String> performanceLabels = new ArrayList<>();
        List<Number> performanceData = new ArrayList<>();
        int currentYear = Year.now().getValue();
        for (int year = currentYear - 2; year <= currentYear; year++) { // Last 3 years including current
            performanceLabels.add(String.valueOf(year));
            performanceData.add(studentRepository.countStudentsByYearAndSchoolAdminId(schoolAdminId, year));
        }
        dashboardData.setStudentPerformanceData(new ChartDataDTO(performanceLabels, performanceData));

        // Boys vs Girls Enrollment Data (over years)
        List<String> genderLabels = new ArrayList<>();
        List<Number> boysData = new ArrayList<>();
        List<Number> girlsData = new ArrayList<>();
        for (int year = currentYear - 4; year <= currentYear; year++) { // Last 5 years including current
            genderLabels.add(String.valueOf(year));
            boysData.add(studentRepository.countByGenderAndYearAndSchoolAdminId(schoolAdminId, "Male", year));
            girlsData.add(studentRepository.countByGenderAndYearAndSchoolAdminId(schoolAdminId, "Female", year));
        }
        dashboardData.setGenderEnrollmentData(new GenderChartDataDTO(genderLabels, boysData, girlsData));

        return dashboardData;
    }
}
