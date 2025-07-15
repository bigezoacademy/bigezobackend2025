package bigezo.code.backend.repository;

import bigezo.code.backend.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findBySchoolAdminId(Long schoolAdminId);
    Optional<Subscription> findByTransactionId(String transactionId);
}
