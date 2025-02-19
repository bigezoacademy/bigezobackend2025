package bigezo.code.backend.repository;

import bigezo.code.backend.model.SchoolFeesSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolFeesSettingRepository extends JpaRepository<SchoolFeesSetting, Long> {
}
