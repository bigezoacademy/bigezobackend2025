package bigezo.code.backend.controller;

import bigezo.code.backend.model.Subscription;
import bigezo.code.backend.model.TransactionStatusResponse;
import bigezo.code.backend.service.PesapalService;
import bigezo.code.backend.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final PesapalService pesapalService;

    public SubscriptionController(SubscriptionService subscriptionService, PesapalService pesapalService) {
        this.subscriptionService = subscriptionService;
        this.pesapalService = pesapalService;
    }

    @PostMapping("/free")
    public ResponseEntity<Subscription> createFreeSubscription(@RequestParam Long schoolAdminId) {
        Subscription subscription = subscriptionService.createFreeSubscription(schoolAdminId);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/paid")
    public ResponseEntity<String> createPaidSubscription(@RequestParam Long schoolAdminId, @RequestParam int numberOfStudents, @RequestParam String tierName) {
        String paymentUrl = subscriptionService.createPaidSubscription(schoolAdminId, numberOfStudents, tierName);
        return ResponseEntity.ok(paymentUrl);
    }

    @GetMapping("/status")
    public ResponseEntity<Subscription> getSubscriptionStatus(@RequestParam Long schoolAdminId) {
        Optional<Subscription> subscription = subscriptionService.getSubscriptionStatus(schoolAdminId);
        return subscription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> subscriptionCallback(@RequestParam String OrderTrackingId) {
        try {
            TransactionStatusResponse transactionStatus = pesapalService.getTransactionStatus(OrderTrackingId);
            String status = transactionStatus.getPaymentStatusDescription();

            Subscription.PaymentStatus paymentStatus;
            if ("Completed".equalsIgnoreCase(status)) {
                paymentStatus = Subscription.PaymentStatus.COMPLETED;
            } else if ("Failed".equalsIgnoreCase(status)) {
                paymentStatus = Subscription.PaymentStatus.FAILED;
            } else {
                paymentStatus = Subscription.PaymentStatus.PENDING;
            }

            subscriptionService.updateSubscriptionStatus(OrderTrackingId, paymentStatus);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
