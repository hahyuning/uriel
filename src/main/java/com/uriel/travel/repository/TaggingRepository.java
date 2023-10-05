package com.uriel.travel.repository;

import com.uriel.travel.domain.Tagging;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaggingRepository extends JpaRepository<Tagging, Long> {
}
