package bigezo.code.backend.controller;

import bigezo.code.backend.model.PaymentStatus;
import bigezo.code.backend.service.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment-status")
public class PaymentStatusController {
    
    private final PaymentStatusService paymentStatusService;
    
    @Autowired
    public PaymentStatusController(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
    }
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentStatus> getPaymentStatus(@PathVariable String paymentId) {
        PaymentStatus status = paymentStatusService.getPaymentStatus(paymentId);
        return ResponseEntity.ok(status);
    }
}
