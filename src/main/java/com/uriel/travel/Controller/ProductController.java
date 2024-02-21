package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.dto.BatchRequestDto;
import com.uriel.travel.dto.product.ProductDetailResponseDto;
import com.uriel.travel.dto.product.ProductRequestDto;
import com.uriel.travel.dto.filterCond.ProductFilter;
import com.uriel.travel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    // 상품 임시저장
    @PostMapping("/temp-create")
    public BaseResponse<Void> temporarySave(@RequestBody ProductRequestDto.Create requestDto) {
        productService.temporarySave(requestDto);

        return BaseResponse.ok();
    }

    // 상품 임시저장 업데이트
    @PutMapping("/temp-update/{productId}")
    public BaseResponse<Void> temporaryUpdate(@RequestBody ProductRequestDto.Update requestDto, @PathVariable Long productId) {
        productService.temporaryUpdate(requestDto, productId);

        return BaseResponse.ok();
    }

    // 상품 등록
    @PostMapping("/create")
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
    public BaseResponse<Void> delete(@RequestBody BatchRequestDto requestDto) {

        List<Long> ids = requestDto.getIds();
        productService.delete(ids);
        return BaseResponse.ok();
    }

    // 상품 공개/비공개 처리
    @PostMapping("/batch-update")
    public BaseResponse<Void> privacyUpdate(@RequestBody BatchRequestDto requestDto) {

        productService.privacyUpdate(requestDto.getOperation(), requestDto.getIds());
        return BaseResponse.ok();
    }

    // 상품 복제
    @PostMapping("/duplicate/{productId}")
    public BaseResponse<Void> duplicate(@PathVariable Long productId) {
        productService.duplicate(productId);
        return BaseResponse.ok();
    }

    // 패키지 별 상품 목록 조회
    @PostMapping("/search")
    public BaseResponse<Page<ProductFilter.ProductFilterResponseDto>> searchByPackage(@RequestBody ProductFilter.ProductFilterCond filterCond) {
        return BaseResponse.ok(productService.searchByPackage(filterCond));
    }

    // 상품 상세확인
    @GetMapping("/{productId}")
    public BaseResponse<ProductDetailResponseDto> productDetail(@PathVariable Long productId) {
        return BaseResponse.ok(productService.productDetail(productId));
    }
}
