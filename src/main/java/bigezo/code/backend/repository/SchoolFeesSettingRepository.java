package bigezo.code.backend.repository;

import bigezo.code.backend.model.SchoolFeesSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolFeesSettingRepository extends JpaRepository<SchoolFeesSetting, Long> {

    @Query("SELECT s.id FROM SchoolFeesSetting s WHERE s.year = :year AND s.term = :term AND s.level = :level")
    Long findIdByYearAndTermAndLevel(@Param("year") int year, @Param("term") int term, @Param("level") String level);

    @Query("SELECT s FROM SchoolFeesSetting s WHERE s.year = :year AND s.schoolAdmin.id = :schoolAdminId")
    List<SchoolFeesSetting> findByYearAndSchoolAdminId(@Param("year") int year, @Param("schoolAdminId") Long schoolAdminId);

    @Query("SELECT s.id FROM SchoolFeesSetting s WHERE s.year = :year AND s.term = :term AND s.level = :level AND s.schoolAdmin.id = :schoolAdminId")
    Long findByYearTermLevelSchoolAdminId(@Param("year") int year, @Param("term") int term, @Param("level") String level, @Param("schoolAdminId") Long schoolAdminId);

}
