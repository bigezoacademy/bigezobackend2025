package bigezo.code.backend.repository;

import bigezo.code.backend.model.SchoolFeesSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolFeesSettingRepository extends JpaRepository<SchoolFeesSetting, Long> {

    @Query("SELECT s.id FROM SchoolFeesSetting s WHERE s.year = :year AND s.term = :term AND s.level = :level")
    Long findIdByYearAndTermAndLevel(@Param("year") int year, @Param("term") int term, @Param("level") String level);
}
