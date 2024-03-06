package com.uriel.travel.service;

import com.uriel.travel.domain.OrderState;
import com.uriel.travel.domain.dto.order.OrderFilter;
import com.uriel.travel.domain.dto.order.OrderRequestDto;
import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.domain.dto.order.TravelerInfo;
import com.uriel.travel.domain.entity.Order;
import com.uriel.travel.domain.entity.Product;
import com.uriel.travel.domain.entity.Traveler;
import com.uriel.travel.domain.entity.User;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final OrderRepositoryCustomImpl orderRepositoryCustom;

    // 사용자 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderFilter.OrderFilterResponseDto> getMyOrders(String loginUserId, OrderFilter.OrderFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), 10);
        return orderRepositoryCustom.findByReserveUser(filterCond, loginUserId, pageRequest);
    }

    // 주문 정보 상세 조회
    @Transactional(readOnly = true)
    public OrderResponseDto.OrderInfo getOrderInfo(String imomOrderId) {
        OrderResponseDto.OrderInfo orderResponseDto = OrderResponseDto.OrderInfo.of(orderRepository.findByImomOrderId(imomOrderId));

        List<Traveler> travelerList = travelerRepository.findByImomOrderId(imomOrderId);
        List<TravelerInfo> travelerInfos = travelerList.stream().map(TravelerInfo::new).collect(Collectors.toList());
        orderResponseDto.setTravelerInfos(travelerInfos);

        return orderResponseDto;
    }

    // 관리자용 주문 목록 조회
    @Transactional(readOnly = true)
    public Page<OrderFilter.OrderFilterResponseDtoForAdmin> getOrderListForAdmin(OrderFilter.OrderFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), 10);
        return orderRepositoryCustom.ordersByFilter(filterCond, pageRequest);
    }

    // 주문 목록 검색
    @Transactional(readOnly = true)
    public Page<OrderFilter.OrderFilterResponseDtoForAdmin> orderSearch(OrderFilter.OrderSearchCond searchCond) {
        PageRequest pageRequest = PageRequest.of(searchCond.getOffset(), 10);
        return orderRepositoryCustom.searchOrder(searchCond, pageRequest);
    }

    // 주문 등록 (토스 연동)
    public void createOrder(JSONObject jsonObject, OrderRequestDto.Create requestDto, String email) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        Order order = Order.builder()
                .orderDate(LocalDateTime.parse((String) jsonObject.get("approvedAt"), formatter))
                .adultCount(requestDto.getAdultCount())
                .childCount(requestDto.getChildCount())
                .infantCount(requestDto.getChildCount())
                .totalCount(requestDto.getTotalCount())
                .totalPrice(requestDto.getTotalPrice())
                .payedPrice((Long) jsonObject.get("totalAmount"))
                .orderState(OrderState.RESERVED)
                .build();

        order.addOrderNumber((String) jsonObject.get("orderId"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        order.setReserveUser(user);

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND));
        order.setProduct(product);
        orderRepository.save(order);

        requestDto.getTravelerInfoList()
                .forEach(travelerInfo -> {
                    Traveler traveler = travelerInfo.toEntity();
                    traveler.setOrder(order);
                    travelerRepository.save(traveler);
                });
    }

    // 잔금 완납
    public void additionalPayment(JSONObject jsonObject, OrderRequestDto.AdditionalPayment requestDto) {
        Order order = orderRepository.findByImomOrderId(requestDto.getImomOrderId());

        order.addOrderNumber((String) jsonObject.get("orderId"));
        order.additionalPayment((Long) jsonObject.get("totalAmount"));
    }

    // 여행자 정보 변경
    public void updateTravelers(OrderRequestDto.UpdateTraveler requestDto) {

    }
}
