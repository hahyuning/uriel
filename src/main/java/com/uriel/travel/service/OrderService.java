package com.uriel.travel.service;

import com.uriel.travel.domain.dto.order.OrderFilter;
import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.domain.dto.order.TravelerInfo;
import com.uriel.travel.domain.entity.Traveler;
import com.uriel.travel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final OrderRepositoryCustomImpl orderRepositoryCustom;

    // TODO: 실제 주문 등록 처리

    // 주문 정보 등록 (테스트용)
//    public OrderResponseDto.OrderInfo testCreateOrder(OrderRequestDto.Create requestDto) {
//        Date date = new Date();
//        Product product = productRepository.findById(requestDto.getProductId())
//                .orElseThrow(() ->
//                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PACKAGE));
//
//        User user = userRepository.findByEmail("hahyuning@naver.com")
//                .orElseThrow(() ->
//                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
//
//        Order order = Order
//                .builder()
//                .orderNumber(requestDto.getOrderNumber())
//                .orderDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
//                .adultCount(requestDto.getAdultCount())
//                .childCount(requestDto.getChildCount())
//                .infantCount(requestDto.getInfantCount())
//                .build();
//
//        order.setProduct(product);
//
//        orderRepository.save(order);
//
//        requestDto.getTravelerInfoList()
//                .forEach(travelerInfo -> {
//                    Traveler traveler = travelerInfo.toEntity();
//                    traveler.setOrder(order);
//                    travelerRepository.save(traveler);
//                });
//
//        return OrderResponseDto.OrderInfo.of(order);
//    }

    // 사용자 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderFilter.OrderFilterResponseDto> getMyOrders(String loginUserId, OrderFilter.OrderFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), 10);
        return orderRepositoryCustom.findByReserveUser(filterCond, loginUserId, pageRequest);
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
}
