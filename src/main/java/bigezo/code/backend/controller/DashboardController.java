package bigezo.code.backend.controller;

import bigezo.code.backend.dto.DashboardDataDTO;
import bigezo.code.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardDataDTO> getDashboardData(@RequestParam Long schoolAdminId) {
        DashboardDataDTO data = dashboardService.getDashboardData(schoolAdminId);
        return ResponseEntity.ok(data);
    }
}
