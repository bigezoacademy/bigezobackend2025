package bigezo.code.backend.service;

import bigezo.code.backend.dto.PaymentRequestDTO;
import bigezo.code.backend.dto.BillingAddressDTO;
import bigezo.code.backend.model.PaymentResponse;
import bigezo.code.backend.model.Pricing;
import bigezo.code.backend.model.SchoolAdmin;
import bigezo.code.backend.model.Subscription;
import bigezo.code.backend.model.BillingAddress;
import bigezo.code.backend.repository.BillingAddressRepository;
import bigezo.code.backend.repository.PricingRepository;
import bigezo.code.backend.repository.SchoolAdminRepository;
import bigezo.code.backend.repository.SubscriptionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PricingRepository pricingRepository;
    private final SchoolAdminRepository schoolAdminRepository;
    private final PaymentService paymentService;
    private final ObjectMapper objectMapper;
    private final BillingAddressRepository billingAddressRepository;

    @Value("${pesapal.ipn}")
    private String pesapalIpn;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, PricingRepository pricingRepository, SchoolAdminRepository schoolAdminRepository, PaymentService paymentService, ObjectMapper objectMapper, BillingAddressRepository billingAddressRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.pricingRepository = pricingRepository;
        this.schoolAdminRepository = schoolAdminRepository;
        this.paymentService = paymentService;
        this.objectMapper = objectMapper;
        this.billingAddressRepository = billingAddressRepository;
    }

    public Subscription createFreeSubscription(Long schoolAdminId) {
        SchoolAdmin schoolAdmin = schoolAdminRepository.findById(schoolAdminId).orElseThrow(() -> new RuntimeException("School admin not found"));
        Subscription subscription = new Subscription();
        subscription.setSchoolAdmin(schoolAdmin);
        subscription.setSubscriptionType(Subscription.SubscriptionType.FREE);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusYears(1)); // Or whatever duration is appropriate
        subscription.setPaymentStatus(Subscription.PaymentStatus.COMPLETED);
        return subscriptionRepository.save(subscription);
    }

    public String createPaidSubscription(Long schoolAdminId, int numberOfStudents, String tierName) {
        SchoolAdmin schoolAdmin = schoolAdminRepository.findById(schoolAdminId).orElseThrow(() -> new RuntimeException("School admin not found"));
        Pricing pricing = pricingRepository.findByTier(tierName).orElseThrow(() -> new RuntimeException("Pricing not found for tier: " + tierName));
        BigDecimal totalCost = pricing.getCostPerStudent().multiply(new BigDecimal(numberOfStudents));

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setId(UUID.randomUUID().toString()); // Generate a unique ID for merchant reference
        paymentRequest.setAmount(totalCost.doubleValue());
        paymentRequest.setCurrency("KES"); // Or get from config
        paymentRequest.setDescription("Bigezo Subscription for " + schoolAdmin.getSchoolName());
        paymentRequest.setCallbackUrl("http://your-frontend.com/subscription/callback"); // This should be a configurable URL
        paymentRequest.setNotificationId(pesapalIpn); // Set the Pesapal IPN ID

        // Fetch billing address and set it in the paymentRequest
        BillingAddress billingAddress = billingAddressRepository.findBySchoolAdminId(schoolAdminId)
                .orElseThrow(() -> new RuntimeException("Billing address not found for school admin: " + schoolAdminId));
        
        // Convert BillingAddress entity to BillingAddressDTO
        BillingAddressDTO billingAddressDTO = new BillingAddressDTO();
        billingAddressDTO.setEmailAddress(billingAddress.getEmailAddress());
        billingAddressDTO.setPhoneNumber(billingAddress.getPhoneNumber());
        billingAddressDTO.setCountryCode(billingAddress.getCountryCode());
        billingAddressDTO.setFirstName(billingAddress.getFirstName());
        billingAddressDTO.setMiddleName(billingAddress.getMiddleName());
        billingAddressDTO.setLastName(billingAddress.getLastName());
        billingAddressDTO.setLine1(billingAddress.getLine1());
        billingAddressDTO.setLine2(billingAddress.getLine2());
        billingAddressDTO.setCity(billingAddress.getCity());
        billingAddressDTO.setState(billingAddress.getState());
        billingAddressDTO.setPostalCode(billingAddress.getPostalCode());
        billingAddressDTO.setZipCode(billingAddress.getZipCode());

        paymentRequest.setBillingAddress(billingAddressDTO);

        try {
            String paymentResponseString = paymentService.makePayment(paymentRequest);
            PaymentResponse paymentResponse = objectMapper.readValue(paymentResponseString, PaymentResponse.class);

            if (paymentResponse.getStatus().equals("200") && paymentResponse.getError() == null) {
                Subscription subscription = new Subscription();
                subscription.setSchoolAdmin(schoolAdmin);
                subscription.setSubscriptionType(Subscription.SubscriptionType.PAID);
                subscription.setNumberOfStudents(numberOfStudents);
                subscription.setCostPerStudent(pricing.getCostPerStudent());
                subscription.setTotalCost(totalCost);
                subscription.setStartDate(LocalDate.now());
                subscription.setEndDate(LocalDate.now().plusYears(1));
                subscription.setPaymentStatus(Subscription.PaymentStatus.PENDING);
                subscription.setTransactionId(paymentResponse.getOrderTrackingId()); // Set the transaction ID here

                subscriptionRepository.save(subscription);

                return paymentResponse.getRedirectUrl();
            } else {
                throw new RuntimeException("Payment initiation failed: " + paymentResponse.getError().getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Payment failed: " + e.getMessage());
        }
    }

    public Optional<Subscription> getSubscriptionStatus(Long schoolAdminId) {
        return subscriptionRepository.findBySchoolAdminId(schoolAdminId);
    }

    public void updateSubscriptionStatus(String orderTrackingId, Subscription.PaymentStatus status) {
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByTransactionId(orderTrackingId);
        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            subscription.setPaymentStatus(status);
            subscriptionRepository.save(subscription);
        } else {
            throw new RuntimeException("Subscription not found for transaction ID: " + orderTrackingId);
        }
    }

}
