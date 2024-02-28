package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.filterCond.OrderFilter;
import com.uriel.travel.domain.dto.order.OrderRequestDto;
import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.service.OrderService;
import com.uriel.travel.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 정보 등록 (테스트용)
    @PostMapping("/test/create")
    public BaseResponse<OrderResponseDto.OrderInfo> testCreateOrder(@RequestBody OrderRequestDto.Create requestDto) {
        return BaseResponse.ok(orderService.testCreateOrder(requestDto));
    }

    // 사용자 주문 목록 조회
    @GetMapping("/{offset}")
    public BaseResponse<List<OrderResponseDto.MyOrder>> getMyOrders(@ModelAttribute OrderFilter.OrderFilterCond filterCond) {
        return BaseResponse.ok(orderService.getMyOrders(SecurityUtil.getCurrentUsername(), filterCond));
    }

    // 주문 정보 상세 조회
    @GetMapping("/detail/{orderId}")
    public BaseResponse<OrderResponseDto.OrderInfo> getOrderInfo(@PathVariable String orderId) {
        return BaseResponse.ok(orderService.getOrderInfo(orderId));
    }

    // 관리자 주문 목록 조회

}
