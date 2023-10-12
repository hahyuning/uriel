package com.uriel.travel.repository;

import com.uriel.travel.dto.ProductFilterResponseDto;
import com.uriel.travel.dto.ProductRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<ProductFilterResponseDto> searchByFilterCond(ProductRequestDto.FilterCond filterCond, Pageable pageable);
}
