package bigezo.code.backend.repository;

import bigezo.code.backend.model.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequirementRepository extends JpaRepository<Requirement, Long> {
    List<Requirement> findByYearAndLevelAndTerm(int year, String level, int term);
}
