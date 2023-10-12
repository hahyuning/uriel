package com.uriel.travel.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uriel.travel.domain.QProduct;
import com.uriel.travel.dto.ProductFilterResponseDto;
import com.uriel.travel.dto.ProductRequestDto;
import com.uriel.travel.dto.QProductFilterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QProduct product = QProduct.product;

    @Override
    public Page<ProductFilterResponseDto> searchByFilterCond(ProductRequestDto.FilterCond filterCond, Pageable pageable) {
        List<ProductFilterResponseDto> productList = jpaQueryFactory
                .select(new QProductFilterResponseDto(
                        product.startDate,
                        product.endDate,
                        product.airline,
                        product.price,
                        product.maxCount,
                        product.nowCount,
                        product.productState
                ))
                .from(product)
                .where(
                        packageIdEq(filterCond),
                        productStateEq(filterCond),
                        startDateBtw(filterCond)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(
                        packageIdEq(filterCond),
                        productStateEq(filterCond),
                        startDateBtw(filterCond))
                .fetchOne();

        return new PageImpl<>(productList, pageable, count);
    }

    private BooleanExpression packageIdEq(ProductRequestDto.FilterCond filterCond) {

        if(filterCond.getPackageId() == null)  return null;
        return product.aPackage.id.eq(filterCond.getPackageId());
    }

    private BooleanExpression productStateEq(ProductRequestDto.FilterCond filterCond) {

        if(filterCond.getProductState() == null)  return null;
        return product.productState.eq(filterCond.getProductState());
    }

    private BooleanExpression startDateBtw(ProductRequestDto.FilterCond filterCond) {

        if(filterCond.getStartDateMax() == null || filterCond.getStartDateMin() == null)  return null;
        return product.startDate.between(filterCond.getStartDateMin(), filterCond.getStartDateMax());

    }
}
