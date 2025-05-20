package bigezo.code.backend.controller;

import bigezo.code.backend.dto.PaymentRequestDTO;
import bigezo.code.backend.model.Payment;
import bigezo.code.backend.model.BillingAddress;
import bigezo.code.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequestDTO requestDTO) {
        // Convert DTO to entity
        Payment payment = new Payment();
        payment.setPesapalId(requestDTO.getId());
        payment.setCurrency(requestDTO.getCurrency());
        payment.setAmount(requestDTO.getAmount());
        payment.setDescription(requestDTO.getDescription());
        payment.setCallbackUrl(requestDTO.getCallbackUrl());
        payment.setRedirectMode(requestDTO.getRedirectMode());
        payment.setNotificationId(requestDTO.getNotificationId());
        payment.setBranch(requestDTO.getBranch());

        // Create and set billing address
        if (requestDTO.getBillingAddress() != null) {
            BillingAddress billingAddress = new BillingAddress();
            billingAddress.setEmailAddress(requestDTO.getBillingAddress().getEmailAddress());
            billingAddress.setPhoneNumber(requestDTO.getBillingAddress().getPhoneNumber());
            billingAddress.setCountryCode(requestDTO.getBillingAddress().getCountryCode());
            billingAddress.setFirstName(requestDTO.getBillingAddress().getFirstName());
            billingAddress.setMiddleName(requestDTO.getBillingAddress().getMiddleName());

            payment.setBillingAddress(billingAddress);
        }

        try {
            String response = paymentService.makePayment(payment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing payment request: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}