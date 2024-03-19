package com.uriel.travel.service;

import com.uriel.travel.domain.OrderState;
import com.uriel.travel.domain.ProductState;
import com.uriel.travel.domain.dto.order.OrderFilter;
import com.uriel.travel.domain.dto.order.OrderRequestDto;
import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.domain.dto.order.TravelerInfo;
import com.uriel.travel.domain.dto.toss.WebHookInfo;
import com.uriel.travel.domain.entity.*;
import com.uriel.travel.exception.CustomBadRequestException;
import com.uriel.travel.exception.CustomNotFoundException;
import com.uriel.travel.exception.ErrorCode;
import com.uriel.travel.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final ProductDetailRepository productDetailRepository;

    private final OrderRepositoryCustomImpl orderRepositoryCustom;

    // 사용자 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderFilter.OrderFilterResponseDto> getMyOrders(String loginUserId, OrderFilter.OrderFilterCond filterCond) {
        PageRequest pageRequest = PageRequest.of(filterCond.getOffset(), 10);
        return orderRepositoryCustom.findByReserveUser(filterCond, loginUserId, pageRequest);
    }

    // 주문 정보 상세 조회 (관리자)
    @Transactional(readOnly = true)
    public OrderResponseDto.OrderInfo getOrderInfo(String imomOrderId) {
        Order order = orderRepository.findByImomOrderId(imomOrderId);
        OrderResponseDto.OrderInfo orderResponseDto = OrderResponseDto.OrderInfo.of(order);

        orderResponseDto.setReserveUserInfo(userRepository.findByEmail(order.getReserveUserEmail())
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER)));
        orderResponseDto.setProductInfo(productRepository.findById(order.getProductId())
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT)));

        List<Traveler> travelerList = travelerRepository.findByImomOrderId(imomOrderId);
        List<TravelerInfo> travelerInfos = travelerList.stream().map(TravelerInfo::new).collect(Collectors.toList());
        orderResponseDto.setTravelerInfos(travelerInfos);

        return orderResponseDto;
    }

    // 주문 정보 상세 조회 (사용자)
    @Transactional(readOnly = true)
    public OrderResponseDto.MyOrder getMyOrderInfo(String imomOrderId) {
        Order order = orderRepository.findByImomOrderId(imomOrderId);
        OrderResponseDto.MyOrder orderResponseDto = OrderResponseDto.MyOrder.of(order);

        orderResponseDto.setReserveUserInfo(userRepository.findByEmail(order.getReserveUserEmail())
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER)));
        orderResponseDto.setProductInfo(productRepository.findById(order.getProductId())
                .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT)));

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

//    // 주문 목록 검색
//    @Transactional(readOnly = true)
//    public Page<OrderFilter.OrderFilterResponseDtoForAdmin> orderSearch(OrderFilter.OrderSearchCond searchCond) {
//        PageRequest pageRequest = PageRequest.of(searchCond.getOffset(), 10);
//        return orderRepositoryCustom.searchOrder(searchCond, pageRequest);
//    }

    // 주문 등록
    public String createOrder(JSONObject jsonObject, OrderRequestDto.Create requestDto, String email) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        // Order 객체 생성
        Order order = Order.builder()
                .imomOrderId((String) jsonObject.get("orderId"))
                .orderDate(LocalDateTime.parse((String) jsonObject.get("requestedAt"), formatter))
                .adultCount(requestDto.getAdultCount())
                .childCount(requestDto.getChildCount())
                .infantCount(requestDto.getChildCount())
                .totalCount(requestDto.getTotalCount())
                .totalPrice(requestDto.getTotalPrice())
                .payedPrice((Long) jsonObject.get("totalAmount"))
                .build();

        // 결제 방법
        String method = (String) jsonObject.get("method");
        if (method.equals("가상계좌")) {
            order.setOrderState(OrderState.READY);
        } else {
            order.setOrderState(OrderState.RESERVED);
        }

        // 결제 정보 추가
        order.addOrderNumber((String) jsonObject.get("orderId"));

        // reserve user 연관관계
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER));
        order.setReserveUser(user);

        // product 연관관계
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT));
        product.updateNowCount(requestDto.getTotalCount());

        order.setProduct(product);

        // order 객체 저장
        orderRepository.save(order);

        log.info("주문 정보 저장 완료");

        // 여행자 정보 저장
        requestDto.getTravelerInfoList()
                .forEach(travelerInfo -> {
                    Traveler traveler = travelerInfo.toEntity();
                    traveler.setOrder(order);
                    travelerRepository.save(traveler);
                });

        return order.getImomOrderId();
    }

    // 잔금 완납
    public void additionalPayment(JSONObject jsonObject, OrderRequestDto.AdditionalPayment requestDto) {
        Order order = orderRepository.findByImomOrderId(requestDto.getImomOrderId());

        order.addOrderNumber((String) jsonObject.get("orderId"));
        order.additionalPayment((Long) jsonObject.get("totalAmount"));
    }

    // 여행자 정보 변경
    public void updateTravelers(OrderRequestDto.UpdateTraveler requestDto) {
        Order order = orderRepository.findByImomOrderId(requestDto.getImomOrderId());

        int adultCount = order.getAdultCount();
        int childCount = order.getChildCount();
        int infantCount = order.getInfantCount();

        int newAdultCount = requestDto.getAdultCount();
        int newChildCount = requestDto.getChildCount();
        int newInfantCount = requestDto.getInfantCount();

        if (adultCount != newAdultCount || childCount != newChildCount || infantCount != newInfantCount) {
            ProductDetail productDetail = productDetailRepository.findByProductId(order.getProductId());
            order.updateTravelerAndPrice(requestDto, productDetail);
        }

        travelerRepository.deleteAll(order.getTravelerList());

        requestDto.getTravelerInfoList()
                .forEach(travelerInfo -> {
                    Traveler traveler = travelerInfo.toEntity();
                    traveler.setOrder(order);
                    travelerRepository.save(traveler);
                });


        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT));
        product.updateNowCount(requestDto.getTotalCount() - order.getTotalCount());
    }

    // 주문 취소
    public void cancelOrder(String imomOrderId) {
        Order order = orderRepository.findByImomOrderId(imomOrderId);
        order.cancel(order.getPayedPrice());

        Product product = productRepository.findById(order.getProductId())
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT));
        product.updateNowCount(-1 * (order.getTotalCount()));
    }

    // 추가금 변경
    public void updateAdditionalPrice(OrderRequestDto.UpdateAdditionalPrice requestDto) {
        Order order = orderRepository.findByImomOrderId(requestDto.getImomOrderId());
        order.updateAdditionalPrice(requestDto.getAdditionalPrice());
    }

    // 예약가능여부 확인 (인원 확인)
    public void checkReservedUserCount(int totalCount, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT));

        if (product.getProductState().equals(ProductState.RESERVATION_DEADLINE)) {
            log.info("예약 마감");
            throw new CustomBadRequestException(ErrorCode.RESERVATION_NOT_AVAILABLE);
        }

        if (product.getNowCount() + totalCount > product.getMaxCount()) {
            log.info("예약 인원 초과");
            throw new CustomBadRequestException(ErrorCode.EXCEED_MAX_COUNT);
        }
    }

    public void updateOrderStateByVAWebHook(WebHookInfo.VirtualAccount requestDto, JSONObject jsonObject) {
        // done -> 입금 완료
        // canceled -> 취소
        // partial canceled -> 부분 취소
        Order order = orderRepository.findByTossOrderId(requestDto.getOrderId());
        String status = requestDto.getStatus();

        log.info("가상계좌 결제 상태: " + status);

        if (status.equals("DONE")) { // 가상계좌 입금 완료
            order.additionalPayment((Long) jsonObject.get("totalAmount"));
        } else if (status.equals("CANCELED") || status.equals("PARTIAL_CANCELED")) { // 가상계좌 주문 취소

            JSONArray cancels = (JSONArray) jsonObject.get("cancels");
            String lastTransactionKey = (String) jsonObject.get("lastTransactionKey");

            log.info("cancels 배열  " + cancels.toString());
            log.info("lastTransactionKey  " + lastTransactionKey);

            for (int i = 0; i < cancels.size(); i++) {

                JSONObject cancel = (JSONObject) cancels.get(i);
                String transactionKey = (String) cancel.get("transactionKey");

                log.info("transactionKey  " + transactionKey);

                if (transactionKey.equals(lastTransactionKey)) {
                    order.cancel((Long) cancel.get("cancelAmount"));
                    log.info("취소 처리 완료");
                }
            }
        }
    }

    public void updateOrderStateByOPWebHook(WebHookInfo.OtherPayments requestDto) {
        // canceled -> 취소
        // partial canceled -> 부분 취소
        String status = requestDto.getData().getStatus();
        Order order = orderRepository.findByTossOrderId(requestDto.getData().getOrderId());

        if (status.equals("CANCELED") || status.equals("PARTIAL_CANCELED")) {
            String lastTransactionKey = requestDto.getData().getLastTransactionKey();
             Arrays.stream(requestDto.getData().getCancels()).toList()
                             .forEach(cancel -> {
                                 if (cancel.getTransactionKey().equals(lastTransactionKey)) {
                                     order.cancel(cancel.getCancelAmount());
                                 }
                             });
        }
    }

    public List<OrderResponseDto.OrderInfo> getAllOrderInfos() {
        List<OrderResponseDto.OrderInfo> result = new ArrayList<>();
        orderRepository.findAll(Sort.by(Sort.Direction.DESC, "orderDate"))
                .forEach(order -> {
                    OrderResponseDto.OrderInfo orderResponseDto = OrderResponseDto.OrderInfo.of(order);

                    orderResponseDto.setReserveUserInfo(userRepository.findByEmail(order.getReserveUserEmail())
                            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_MEMBER)));
                    orderResponseDto.setProductInfo(productRepository.findById(order.getProductId())
                            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.NOT_FOUND_PRODUCT)));

                    result.add(orderResponseDto);
                });
        return result;
    }

    public void updateMemo(OrderRequestDto.UpdateMemo requestDto) {
        orderRepository.findByImomOrderId(requestDto.getImomOrderId())
                .updateMemo(requestDto.getMemo());
    }
}
