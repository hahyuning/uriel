package com.uriel.travel.repository;

import com.uriel.travel.domain.entity.Tagging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaggingRepository extends JpaRepository<Tagging, Long> {

    @Query("SELECT t FROM Tagging t WHERE t.aPackage.id =:packageId")
    List<Tagging> findAllByPackageId(Long packageId);
}
