package bigezo.code.backend.repository;

import bigezo.code.backend.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    List<PaymentStatus> findByItemAndTermAndYearAndSchoolAdminId(String item, String term, String year, Long schoolAdminId);

    List<PaymentStatus> findByStudentIdAndItemAndTermAndYear(Long studentId, String item, String term, String year);
}
