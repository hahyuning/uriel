package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.service.OrderService;
import com.uriel.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    // 주문 정보 등록

    // 주문 목록 조회 (관리자)

    // 주문 정보 검색

    // 주문 정보 수정

    // 사용자 주문 목록 조회
//    @GetMapping("/{offset}")
//    public BaseResponse<List<OrderResponseDto.MyOrder>> getMyOrders(@PathVariable int offset) {
//        return BaseResponse.ok(orderService.getMyOrders(userService.getLoginMemberId(), offset));
//    }

    // 주문 정보 상세 조회
    @GetMapping("/detail/{orderId}")
    public BaseResponse<OrderResponseDto.OrderInfo> getOrderInfo(@PathVariable String orderId) {
        return BaseResponse.ok(orderService.getOrderInfo(orderId));
    }
}
