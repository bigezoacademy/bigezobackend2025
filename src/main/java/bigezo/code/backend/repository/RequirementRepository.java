package bigezo.code.backend.repository;

import bigezo.code.backend.model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findBySchoolAdminIdAndYearAndLevelAndTerm(Long schoolAdminId, int year, String level, int term);

    List<Requirement> findBySchoolAdminId(Long schoolAdminId);

    Optional<Requirement> findByIdAndSchoolAdminId(Long id, Long schoolAdminId);

    void deleteByIdAndSchoolAdminId(Long id, Long schoolAdminId);
    boolean existsByIdAndSchoolAdminId(Long id, Long schoolAdminId);

}
