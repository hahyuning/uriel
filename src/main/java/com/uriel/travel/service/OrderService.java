package com.uriel.travel.service;

import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.domain.dto.order.TravelerInfo;
import com.uriel.travel.domain.entity.Traveler;
import com.uriel.travel.repository.OrderRepository;
import com.uriel.travel.repository.TravelerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final TravelerRepository travelerRepository;

    // 주문 정보 등록

    // 주문 목록 조회 (관리자)

    // 주문 정보 검색

    // 주문 정보 수정

    // 사용자 주문 목록 조회
//    public List<OrderResponseDto.MyOrder> getMyOrders(Long loginUserId, int offset) {
//        PageRequest pageRequest = PageRequest.of(offset, 10);
//        List<Order> orderList = orderRepository.findByReserveUser(loginUserId, pageRequest);
//
//    }

    // 주문 정보 상세 조회
    public OrderResponseDto.OrderInfo getOrderInfo(Long orderId) {
        OrderResponseDto.OrderInfo orderResponseDto = OrderResponseDto.OrderInfo.of(orderRepository.findByOrderId(orderId));

        List<Traveler> travelerList = travelerRepository.findByOrderId(orderId);
        List<TravelerInfo> travelerInfos = travelerList.stream().map(TravelerInfo::new).collect(Collectors.toList());
        orderResponseDto.setTravelerInfos(travelerInfos);

        return orderResponseDto;
    }
}
