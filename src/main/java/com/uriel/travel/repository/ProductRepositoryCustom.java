package com.uriel.travel.repository;

import com.uriel.travel.dto.ProductRequestDto;
import com.uriel.travel.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductResponseDto> searchByFilterCond(ProductRequestDto.FilterCond filterCond, Pageable pageable);
}
