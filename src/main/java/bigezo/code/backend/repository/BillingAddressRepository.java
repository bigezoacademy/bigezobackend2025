package bigezo.code.backend.repository;

import bigezo.code.backend.model.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {
    Optional<BillingAddress> findBySchoolAdminId(Long schoolAdminId);
}