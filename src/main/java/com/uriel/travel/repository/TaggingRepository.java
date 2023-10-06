package com.uriel.travel.repository;

import com.uriel.travel.domain.Tagging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggingRepository extends JpaRepository<Tagging, Long> {
}
