package bigezo.code.backend.repository;

import bigezo.code.backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySchoolAdminId(Long schoolAdminId);
    Optional<Transaction> findByIdAndSchoolAdminId(Long id, Long schoolAdminId);

    Optional<Transaction> findByOrderTrackingId(String orderTrackingId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.schoolAdminId = :schoolAdminId AND t.reason = :reason")
    Double sumAmountByReasonAndSchoolAdminId(@Param("schoolAdminId") Long schoolAdminId, @Param("reason") String reason);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.schoolAdminId = :schoolAdminId AND t.status = 'PENDING'")
    Long countPendingTransactionsBySchoolAdminId(@Param("schoolAdminId") Long schoolAdminId);

}
