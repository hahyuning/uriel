package com.uriel.travel.repository;

import com.uriel.travel.domain.dto.order.OrderFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface OrderRepositoryCustom {

    Page<OrderFilter.OrderFilterResponseDtoForAdmin> ordersByFilter(OrderFilter.OrderFilterCond filterCond, Pageable pageable);

    List<OrderFilter.OrderFilterResponseDto> findByReserveUser(OrderFilter.OrderFilterCond filterCond, String email, Pageable pageable);

//    Page<OrderFilter.OrderFilterResponseDtoForAdmin> searchOrder(OrderFilter.Or filterCond, Pageable pageable);
}
