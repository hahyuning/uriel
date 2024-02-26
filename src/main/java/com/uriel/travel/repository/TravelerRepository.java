package com.uriel.travel.repository;

import com.uriel.travel.domain.entity.Traveler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelerRepository extends JpaRepository<Traveler, Long> {

    @Query("select t from Traveler t where t.order.orderId =:orderId")
    List<Traveler> findByOrderId(Long orderId);
}
