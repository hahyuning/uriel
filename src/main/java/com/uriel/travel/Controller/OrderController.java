package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.order.OrderFilter;
import com.uriel.travel.domain.dto.order.OrderRequestDto;
import com.uriel.travel.domain.dto.order.OrderResponseDto;
import com.uriel.travel.service.OrderService;
import com.uriel.travel.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 정보 등록 (테스트용)
//    @PostMapping("/test/create")
//    public BaseResponse<OrderResponseDto.OrderInfo> testCreateOrder(@RequestBody OrderRequestDto.Create requestDto) {
//        return BaseResponse.ok(orderService.testCreateOrder(requestDto));
//    }

    // 사용자 주문 목록 조회
    @GetMapping
    public BaseResponse<List<OrderFilter.OrderFilterResponseDto>> getMyOrders(@ModelAttribute OrderFilter.OrderFilterCond filterCond) {
        return BaseResponse.ok(orderService.getMyOrders(SecurityUtil.getCurrentUsername(), filterCond));
    }

    // 주문 정보 상세 조회
    @GetMapping("/detail/{imomorderid}")
    public BaseResponse<OrderResponseDto.OrderInfo> getOrderInfo(@PathVariable String imomorderid) {
        return BaseResponse.ok(orderService.getOrderInfo(imomorderid));
    }

    // 주문 정보 상세 조회
    @GetMapping("/myinfo/{imomorderid}")
    public BaseResponse<OrderResponseDto.MyOrder> getMyOrderInfo(@PathVariable String imomorderid) {
        return BaseResponse.ok(orderService.getMyOrderInfo(imomorderid));
    }

    // 관리자 주문 목록 조회
    @PostMapping
    public BaseResponse<Page<OrderFilter.OrderFilterResponseDtoForAdmin>> getOrderListForAdmin(@RequestBody OrderFilter.OrderFilterCond filterCond) {
        return BaseResponse.ok(orderService.getOrderListForAdmin(filterCond));
    }

//    // 주문 정보 검색
//    @GetMapping("/search")
//    public BaseResponse<Page<OrderFilter.OrderFilterResponseDtoForAdmin>> orderSearchForAdmin(@ModelAttribute OrderFilter.OrderSearchCond searchCond) {
//        return BaseResponse.ok(orderService.orderSearch(searchCond));
//    }

    // 여행자 정보 수정
    @PostMapping("/update/travelers")
    public BaseResponse<Void> updateTravelers(@RequestBody OrderRequestDto.UpdateTraveler requestDto) {
        orderService.updateTravelers(requestDto);
        return BaseResponse.ok();
    }

    // 주문취소
    @GetMapping("/cancel/{imomOrderId}")
    public BaseResponse<Void> cancelOrder(@PathVariable String imomOrderId) {
        orderService.cancelOrder(imomOrderId);
        return BaseResponse.ok();
    }

    // 추가금 변경
    @PostMapping("/update/price")
    public BaseResponse<Void> updateAdditionalPrice(@RequestBody OrderRequestDto.UpdateAdditionalPrice requestDto) {
        orderService.updateAdditionalPrice(requestDto);
        return BaseResponse.ok();
    }

    // 엑셀 다운용 전체 목록 조회
    @GetMapping("/excel")
    public BaseResponse<List<OrderResponseDto.OrderInfo>> getAllOrderInfoForExcel() {
        return BaseResponse.ok(orderService.getAllOrderInfos());
    }

    // 메모 변경
    @PostMapping("/update/memo")
    public BaseResponse<Void> updateMemo(@RequestBody OrderRequestDto.UpdateMemo requestDto) {
        orderService.updateMemo(requestDto);
        return BaseResponse.ok();
    }
}
