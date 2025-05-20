package bigezo.code.backend.service;

import bigezo.code.backend.model.Payment;
import bigezo.code.backend.model.PaymentStatus;
import bigezo.code.backend.repository.PaymentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for managing payment statuses.
 */
@Service
public class PaymentStatusService {
    
    private final PaymentStatusRepository paymentStatusRepository;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(PaymentStatusService.class);
    
    @Autowired
    public PaymentStatusService(PaymentStatusRepository paymentStatusRepository, ObjectMapper objectMapper) {
        this.paymentStatusRepository = paymentStatusRepository;
        this.objectMapper = objectMapper;
    }
    
    public PaymentStatus createPaymentStatus(Payment payment, String merchantReference, String redirectUrl) {
        PaymentStatus status = new PaymentStatus();
        status.setPaymentId(payment.getPesapalId());
        status.setStatus("started");
        status.setMerchantReference(merchantReference);
        status.setRedirectUrl(redirectUrl);
        status.setCurrency(payment.getCurrency());
        status.setAmount(payment.getAmount());
        status.setDescription(payment.getDescription());
        status.setCallbackUrl(payment.getCallbackUrl());
        status.setBranch(payment.getBranch());
        status.setNotificationId(payment.getNotificationId());
        try {
            if (payment.getBillingAddress() != null) {
                status.setBillingAddress(objectMapper.writeValueAsString(payment.getBillingAddress()));
            } else {
                status.setBillingAddress(null);
            }
        } catch (Exception e) {
            logger.error("Failed to serialize billing address: {}", e.getMessage());
            status.setBillingAddress(null);
        }
        // Set schoolAdminId and studentId, allowing null values
        status.setSchoolAdminId(payment.getSchoolAdminId() != null ? payment.getSchoolAdminId() : 0L);
        status.setStudentId(payment.getStudentId() != null ? payment.getStudentId() : 0L);
        return paymentStatusRepository.save(status);
    }
    
    public PaymentStatus updatePaymentStatus(String paymentId, String status, String error, String pesapalStatus, String orderTrackingId) {
        PaymentStatus existingStatus = paymentStatusRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment status not found for payment ID: " + paymentId));
        
        existingStatus.setStatus(status);
        existingStatus.setError(error);
        existingStatus.setPaymentStatus(pesapalStatus);
        existingStatus.setOrderTrackingId(orderTrackingId);
        return paymentStatusRepository.save(existingStatus);
    }
    
    public PaymentStatus getPaymentStatus(String paymentId) {
        return paymentStatusRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment status not found for payment ID: " + paymentId));
    }
}
