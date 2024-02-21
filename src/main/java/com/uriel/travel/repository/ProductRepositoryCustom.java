package com.uriel.travel.repository;

import com.uriel.travel.dto.filterCond.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductFilter.ProductFilterResponseDto> searchByFilterCond(ProductFilter.ProductFilterCond filterCond, Pageable pageable);
    Page<ProductFilter.ProductFilterResponseDto> searchByPackage(ProductFilter.ProductFilterCond filterCond, Pageable pageable);
}
