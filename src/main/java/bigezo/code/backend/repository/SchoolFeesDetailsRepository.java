package bigezo.code.backend.repository;

import bigezo.code.backend.model.SchoolFeesDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolFeesDetailsRepository extends JpaRepository<SchoolFeesDetails, Long> {
    List<SchoolFeesDetails> findBySchoolFeesSettingId(Long feesId);
}

