package com.uriel.travel.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uriel.travel.domain.QProduct;
import com.uriel.travel.domain.Release;
import com.uriel.travel.dto.filterCond.ProductFilter;
import com.uriel.travel.dto.filterCond.QProductFilter_ProductFilterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QProduct product = QProduct.product;

//    @Override
//    public Page<ProductFilter.ProductFilterResponseDto> searchByFilterCond(ProductFilter.ProductFilterCond filterCond, Pageable pageable) {
//        List<ProductFilter.ProductFilterResponseDto> productList = jpaQueryFactory
//                .select(new QProductFilter_ProductFilterResponseDto(
//                        product.startDate,
//                        product.endDate,
//                        product.airline,
//                        product.price,
//                        product.maxCount,
//                        product.nowCount,
//                        product.productState
//                )
//                .from(product)
//                .where(
//                        packageIdEq(filterCond),
//                        productStateEq(filterCond),
//                        startDateBtw(filterCond)
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize()))
//                .fetch();
//
//        Long count = jpaQueryFactory
//                .select(product.count())
//                .from(product)
//                .where(
//                        packageIdEq(filterCond),
//                        productStateEq(filterCond),
//                        startDateBtw(filterCond))
//                .fetchOne();
//
//        return new PageImpl<>(productList, pageable, count);
//    }

    @Override
    public Page<ProductFilter.ProductFilterResponseDto> searchByFilterCond(ProductFilter.ProductFilterCond filterCond, Pageable pageable) {
        return null;
    }

    // 패키지별 상품 조회
    @Override
    public Page<ProductFilter.ProductFilterResponseDto> searchByPackage(ProductFilter.ProductFilterCond filterCond, Pageable pageable) {
        List<ProductFilter.ProductFilterResponseDto> productList = jpaQueryFactory
                .select(new QProductFilter_ProductFilterResponseDto(
                        product.id,
                        product.productCode,
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
                        product.isPublic.eq(Release.PUBLIC),
                        startDateAfter(),
                        packageIdEq(filterCond)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.startDate.asc())
                .fetch();

        Long count = jpaQueryFactory
                .select(product.count())
                .from(product)
                .where(
                        startDateAfter(),
                        packageIdEq(filterCond))
                .fetchOne();

        return new PageImpl<>(productList, pageable, count);
    }

    private BooleanExpression packageIdEq(ProductFilter.ProductFilterCond filterCond) {

        if(filterCond.getPackageId() == null)  return null;
        return product.aPackage.id.eq(filterCond.getPackageId());
    }

    private BooleanExpression productStateEq(ProductFilter.ProductFilterCond filterCond) {

        if(filterCond.getProductState() == null)  return null;
        return product.productState.eq(filterCond.getProductState());
    }

    private BooleanExpression startDateBtw(ProductFilter.ProductFilterCond filterCond) {

        if(filterCond.getStartDateMax() == null || filterCond.getStartDateMin() == null)  return null;
        return product.startDate.between(filterCond.getStartDateMin(), filterCond.getStartDateMax());

    }

    private BooleanExpression startDateAfter() {
        return product.startDate.after(LocalDateTime.now());
    }
}
