package bigezo.code.backend.repository;

import bigezo.code.backend.model.Student;
import bigezo.code.backend.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.schoolAdmin.id = :schoolAdminId AND s.year = :year AND s.level = :level AND s.enrollmentStatus = :enrollmentStatus")
    List<Student> findBySchoolAdminIdAndYearAndLevelAndEnrollmentStatus(@Param("schoolAdminId") Long schoolAdminId, 
                                                                       @Param("year") int year, 
                                                                       @Param("level") String level, 
                                                                       @Param("enrollmentStatus") String enrollmentStatus);

    List<Student> findBySchoolAdminId(Long schoolAdminId);

    Optional<Student> findByIdAndSchoolAdminId(Long id, Long schoolAdminId);
    Student findByStudentNumber(String studentNumber);
}
