package bigezo.code.backend.service;

import bigezo.code.backend.model.PaymentStatus;
import bigezo.code.backend.repository.PaymentStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentStatusService {

    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    public List<PaymentStatus> getPaymentStatusByCriteria(String item, String term, String year, Long schoolAdminId) {
        return paymentStatusRepository.findByItemAndTermAndYearAndSchoolAdminId(item, term, year, schoolAdminId);
    }

    public List<PaymentStatus> getPaymentStatusForStudent(Long studentId, String item, String term, String year) {
        return paymentStatusRepository.findByStudentIdAndItemAndTermAndYear(studentId, item, term, year);
    }
}
