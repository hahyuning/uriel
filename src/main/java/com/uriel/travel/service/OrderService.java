package com.uriel.travel.service;

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
    public OrderResponseDto.OrderInfo testCreateOrder(String email, OrderRequestDto.Create requestDto) {
        Date date = new Date();
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PACKAGE));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));

        Order order = Order
                .builder()
                .orderId(requestDto.getOrderId())
                .orderedDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .adultCount(requestDto.getAdultCount())
                .childCount(requestDto.getChildCount())
                .infantCount(requestDto.getInfantCount())
                .build();

        order.setMapping(product, product.getAPackage());
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

    // 잔금 수정
    public OrderResponseDto.OrderInfo updateBalance(OrderRequestDto.Update requestDto) {
        Order order = orderRepository.findByOrderId(requestDto.getOrderId());
        order.updateBalance(requestDto.getBalance());

        OrderResponseDto.OrderInfo orderInfo = OrderResponseDto.OrderInfo.of(order);

        List<TravelerInfo> travelerInfoList = new ArrayList<>();
        order.getTravelerList()
                .forEach(traveler -> {
                    travelerInfoList.add(new TravelerInfo(traveler));
                });
        orderInfo.setTravelerInfos(travelerInfoList);
        return orderInfo;
    }

    // 주문상태 변경
    public OrderResponseDto.OrderInfo updateOrderState(OrderRequestDto.Update requestDto) {
        Order order = orderRepository.findByOrderId(requestDto.getOrderId());
        order.updateOrderState(requestDto.getOrderState());

        OrderResponseDto.OrderInfo orderInfo = OrderResponseDto.OrderInfo.of(order);

        List<TravelerInfo> travelerInfoList = new ArrayList<>();
        order.getTravelerList()
                .forEach(traveler -> {
                    travelerInfoList.add(new TravelerInfo(traveler));
                });
        orderInfo.setTravelerInfos(travelerInfoList);
        return orderInfo;
    }

    // 사용자 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto.MyOrder> getMyOrders(String loginUserId, int offset) {
        List<OrderResponseDto.MyOrder> myOrderList = new ArrayList<>();

        PageRequest pageRequest = PageRequest.of(offset, 10);
        orderRepository.findByReserveUser(loginUserId, pageRequest)
                .forEach(order -> {
                    myOrderList.add(OrderResponseDto.MyOrder
                            .builder()
                            .orderId(order.getOrderId())
                            .orderedDate(order.getOrderedDate())
                            .packageName(order.getAPackage().getPackageName())
                            .startDate(order.getProduct().getStartDate())
                            .endDate(order.getProduct().getEndDate())
                            .totalCount(order.getAdultCount() + order.getChildCount() + order.getInfantCount())
                            .productState(order.getProduct().getProductState().getViewName())
                            .orderState(order.getOrderState().getViewName())
                            .build());
                });
        return myOrderList;
    }

    // 주문 정보 상세 조회
    @Transactional(readOnly = true)
    public OrderResponseDto.OrderInfo getOrderInfo(String orderId) {
        OrderResponseDto.OrderInfo orderResponseDto = OrderResponseDto.OrderInfo.of(orderRepository.findByOrderId(orderId));

        List<Traveler> travelerList = travelerRepository.findByOrderId(orderId);
        List<TravelerInfo> travelerInfos = travelerList.stream().map(TravelerInfo::new).collect(Collectors.toList());
        orderResponseDto.setTravelerInfos(travelerInfos);

        return orderResponseDto;
    }
}
