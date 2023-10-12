package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.ProductRequestDto;
import com.uriel.travel.dto.ProductResponseDto;
import com.uriel.travel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public BaseResponse<Void> create(@RequestBody ProductRequestDto.Create requestDto) {
        productService.create(requestDto);

        return BaseResponse.ok();
    }

    // 상품 수정
    @PutMapping("/{productId}")
    public BaseResponse<Void> update(@RequestBody ProductRequestDto.Update requestDto, @PathVariable Long productId) {
        productService.update(requestDto, productId);

        return BaseResponse.ok();
    }

    // 상품 일괄삭제
    @PostMapping("/batch-delete")
    public BaseResponse<Void> delete(@RequestBody Map<String, List<Long>> param) {
        List<Long> ids = param.get("ids");
        productService.delete(ids);
        return BaseResponse.ok();
    }

    // 상품 복제
    @PostMapping("/batch-duplicate")
    public BaseResponse<Void> duplicate(@RequestBody Map<String, List<Long>> param) {
        List<Long> ids = param.get("ids");
        productService.duplicate(ids);
        return BaseResponse.ok();
    }

    // 패키지 별 상품 목록 조회
    @PostMapping("/search")
    public BaseResponse<Page<ProductResponseDto>> searchByPackage(@RequestBody ProductRequestDto.FilterCond filterCond) {
        return BaseResponse.ok(productService.searchByPackage(filterCond));
    }
}
