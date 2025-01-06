package bigezo.code.backend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolAdminRepository extends JpaRepository<SchoolAdmin, Long> {
    boolean existsByAdminUsername(String adminUsername);
}

