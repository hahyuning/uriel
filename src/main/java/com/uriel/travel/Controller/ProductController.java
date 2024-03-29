package com.uriel.travel.Controller;

import com.uriel.travel.Base.BaseResponse;
import com.uriel.travel.domain.dto.product.ProductFilter;
import com.uriel.travel.domain.dto.BatchRequestDto;
import com.uriel.travel.domain.dto.product.ProductDetailResponseDto;
import com.uriel.travel.domain.dto.product.ProductRequestDto;
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
        productService.temporaryCreate(requestDto);

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

    // 상품 임시저장 -> 저장
    @PutMapping("/save/{productId}")
    public BaseResponse<Void> changeToSaveState(@RequestBody ProductRequestDto.Update requestDto, @PathVariable Long productId) {
        productService.changeToSaveState(requestDto, productId);

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
    @GetMapping("/duplicate/{productId}")
    public BaseResponse<Void> duplicate(@PathVariable Long productId) {
        productService.duplicate(productId);
        return BaseResponse.ok();
    }

    // 패키지 별 상품 목록 조회
    @PostMapping("/search")
    public BaseResponse<Page<ProductFilter.ProductFilterResponseDto>> searchByPackage(@RequestBody ProductFilter.ProductFilterCond filterCond) {
        return BaseResponse.ok(productService.searchByPackage(filterCond));
    }

    // 패키지 별 상품 목록 조회
    @GetMapping("/search/{packageId}")
    public BaseResponse<List<ProductFilter.ProductTwoMonthDate>> getTowMonthData(@PathVariable Long packageId) {
        ProductFilter.ProductFilterCond filterCond = new ProductFilter.ProductFilterCond();
        filterCond.setPackageId(packageId);
        return BaseResponse.ok(productService.getTowMothData(filterCond));
    }

    // 상품 상세확인
    @GetMapping("/{productId}")
    public BaseResponse<ProductDetailResponseDto> productDetail(@PathVariable Long productId) {
        return BaseResponse.ok(productService.productDetail(productId));
    }

    // 상품 상세확인
    @PostMapping
    public BaseResponse<Page<ProductFilter.ProductFilterForAdminResponseDto>> searchForAdmin(@RequestBody ProductFilter.ProductFilterCond filterCond) {
        return BaseResponse.ok(productService.searchForAdmin(filterCond));
    }

}
