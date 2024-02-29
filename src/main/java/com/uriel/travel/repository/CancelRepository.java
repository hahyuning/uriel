package com.uriel.travel.repository;

import com.uriel.travel.domain.entity.Cancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CancelRepository extends JpaRepository<Cancel, String> {
}
