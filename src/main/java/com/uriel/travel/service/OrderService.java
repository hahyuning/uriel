package com.uriel.travel.service;

import com.uriel.travel.domain.dto.filterCond.OrderFilter;
import com.uriel.travel.domain.dto.order.OrderRequestDto;
import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.domain.dto.order.TravelerInfo;
import com.uriel.travel.domain.entity.Order;
import com.uriel.travel.domain.entity.Product;
import com.uriel.travel.domain.entity.Traveler;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.OrderRepository;
import com.uriel.travel.repository.ProductRepository;
import com.uriel.travel.repository.TravelerRepository;
import com.uriel.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final TravelerRepository travelerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // TODO: 실제 주문 등록 처리

    // 주문 정보 등록 (테스트용)
    public OrderResponseDto.OrderInfo testCreateOrder(OrderRequestDto.Create requestDto) {
        Date date = new Date();
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PACKAGE));

        User user = userRepository.findByEmail("hahyuning@naver.com")
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        Order order = Order
                .builder()
                .orderNumber(requestDto.getOrderNumber())
                .orderedDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .adultCount(requestDto.getAdultCount())
                .childCount(requestDto.getChildCount())
                .infantCount(requestDto.getInfantCount())
                .build();

        order.setProduct(product);
        order.partialPayment(requestDto.getTotalPrice());
        order.setReserveUser(user);

        orderRepository.save(order);

        requestDto.getTravelerInfoList()
                .forEach(travelerInfo -> {
                    Traveler traveler = travelerInfo.toEntity();
                    traveler.setOrder(order);
                    travelerRepository.save(traveler);
                });

        return OrderResponseDto.OrderInfo.of(order);
    }

    // 사용자 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto.MyOrder> getMyOrders(String loginUserId, OrderFilter.OrderFilterCond filterCond) {
        List<OrderResponseDto.MyOrder> myOrderList = new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), 10);
        orderRepository.findByReserveUser(loginUserId, pageRequest)
                .forEach(order -> {
                    myOrderList.add(OrderResponseDto.MyOrder.of(order));
                });
        return myOrderList;
    }

    // 주문 정보 상세 조회
    @Transactional(readOnly = true)
    public OrderResponseDto.OrderInfo getOrderInfo(String orderNumber) {
        OrderResponseDto.OrderInfo orderResponseDto = OrderResponseDto.OrderInfo.of(orderRepository.findByOrderNumber(orderNumber));

        List<Traveler> travelerList = travelerRepository.findByorderNumber(orderNumber);
        List<TravelerInfo> travelerInfos = travelerList.stream().map(TravelerInfo::new).collect(Collectors.toList());
        orderResponseDto.setTravelerInfos(travelerInfos);

        return orderResponseDto;
    }
}
