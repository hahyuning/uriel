package com.uriel.travel.repository;

import com.uriel.travel.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {

    @Query("SELECT t FROM Thumbnail t WHERE t.aPackage.id =:packageId")
    List<Thumbnail> findAllByPackageId(Long packageId);
}
