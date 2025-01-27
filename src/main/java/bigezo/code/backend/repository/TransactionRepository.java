package bigezo.code.backend.repository;

import bigezo.code.backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySchoolAdminId(Long schoolAdminId);
    Optional<Transaction> findByIdAndSchoolAdminId(Long id, Long schoolAdminId);

    Optional<Transaction> findByOrderTrackingId(String orderTrackingId);

}
