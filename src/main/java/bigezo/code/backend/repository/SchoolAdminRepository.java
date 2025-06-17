package bigezo.code.backend.repository;

import bigezo.code.backend.model.SchoolAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolAdminRepository extends JpaRepository<SchoolAdmin, Long> {
    boolean existsByAdminUsername(String adminUsername);
}

