package com.uriel.travel.repository;

import com.uriel.travel.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

//    Order findByOrderNumber(String orderNumber);

    @Query("select o from  Order o where o.imomOrderId =:imomOrderId order by o.orderDate desc ")
    Order findByImomOrderId(String imomOrderId);
}
