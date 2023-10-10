package com.uriel.travel.repository;

import com.uriel.travel.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    @Query("SELECT s FROM Schedule s WHERE s.aPackage.id =:packageId")
    List<Schedule> findAllByPackageId(Long packageId);

}
