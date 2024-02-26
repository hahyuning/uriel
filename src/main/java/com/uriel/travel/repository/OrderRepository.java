package com.uriel.travel.repository;

import com.uriel.travel.domain.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderId(Long orderId);

    @Query("select o from Order o where o.reserveUser.id =:userId")
    List<Order> findByReserveUser(Long userId, Pageable pageable);
}
