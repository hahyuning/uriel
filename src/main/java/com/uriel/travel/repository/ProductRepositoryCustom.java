package com.uriel.travel.repository;

import com.uriel.travel.domain.dto.product.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    Page<ProductFilter.ProductFilterResponseDto> searchByPackage(ProductFilter.ProductFilterCond filterCond, Pageable pageable);
    Page<ProductFilter.ProductFilterForAdminResponseDto> searchForAdmin(ProductFilter.ProductFilterCond filterCond, Pageable pageable);
    List<ProductFilter.ProductTwoMonthDate> getTwoMonthProduct(ProductFilter.ProductFilterCond filterCond);
}
