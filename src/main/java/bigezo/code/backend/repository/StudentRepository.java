package bigezo.code.backend.repository;

import bigezo.code.backend.model.Student;
import bigezo.code.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findBySchoolAdminIdAndYearAndLevelAndEnrollmentStatus(Long schoolAdminId, int year, String level, String enrollmentStatus);

    List<Student> findBySchoolAdminId(Long schoolAdminId);

    Optional<Student> findByIdAndSchoolAdminId(Long id, Long schoolAdminId);
    Student findByStudentNumber(String studentNumber);
}
