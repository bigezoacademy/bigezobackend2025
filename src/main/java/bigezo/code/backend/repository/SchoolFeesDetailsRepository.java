package bigezo.code.backend.repository;

import bigezo.code.backend.model.SchoolFeesDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolFeesDetailsRepository extends JpaRepository<SchoolFeesDetails, Long> {
    List<SchoolFeesDetails> findBySchoolFeesSettingId(Long feesId);
}