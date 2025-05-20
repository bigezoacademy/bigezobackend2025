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

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<?> makePayment(@RequestBody PaymentRequestDTO requestDTO) {
        try {
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
                billingAddress.setLastName(requestDTO.getBillingAddress().getLastName());
                billingAddress.setLine1(requestDTO.getBillingAddress().getLine1());
                billingAddress.setLine2(requestDTO.getBillingAddress().getLine2());
                billingAddress.setCity(requestDTO.getBillingAddress().getCity());
                billingAddress.setState(requestDTO.getBillingAddress().getState());
                billingAddress.setPostalCode(requestDTO.getBillingAddress().getPostalCode());
                billingAddress.setZipCode(requestDTO.getBillingAddress().getZipCode());

                payment.setBillingAddress(billingAddress);
            }

            String result = paymentService.makePayment(payment);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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