package bigezo.code.backend.controller;

import bigezo.code.backend.model.PaymentStatus;
import bigezo.code.backend.service.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment-status")
public class PaymentStatusController {

    @Autowired
    private PaymentStatusService paymentStatusService;

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/all")
    public List<PaymentStatus> getPaymentStatusByCriteria(
            @RequestParam String item,
            @RequestParam String term,
            @RequestParam String year,
            @RequestParam Long schoolAdminId) {
        return paymentStatusService.getPaymentStatusByCriteria(item, term, year, schoolAdminId);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/student")
    public List<PaymentStatus> getPaymentStatusForStudent(
            @RequestParam Long studentId,
            @RequestParam String item,
            @RequestParam String term,
            @RequestParam String year) {
        return paymentStatusService.getPaymentStatusForStudent(studentId, item, term, year);
    }
}

