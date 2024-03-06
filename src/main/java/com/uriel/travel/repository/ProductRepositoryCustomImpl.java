package com.uriel.travel.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uriel.travel.domain.ProductState;
import com.uriel.travel.domain.Release;
import com.uriel.travel.domain.SaveState;
import com.uriel.travel.domain.dto.product.ProductFilter;
import com.uriel.travel.domain.dto.product.QProductFilter_ProductFilterForAdminResponseDto;
import com.uriel.travel.domain.dto.product.QProductFilter_ProductFilterResponseDto;
import com.uriel.travel.domain.dto.product.QProductFilter_ProductTwoMonthDate;
import com.uriel.travel.domain.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
                .orderBy(product.startDate.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
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

    @Override
    public List<ProductFilter.ProductTwoMonthDate> getTwoMonthProduct(ProductFilter.ProductFilterCond filterCond) {
        return jpaQueryFactory
                .select(new QProductFilter_ProductTwoMonthDate(
                        product.id,
                        product.startDate
                ))
                .from(product)
                .where(
                        product.isPublic.eq(Release.PUBLIC),
                        startDateBtw(filterCond),
                        packageIdEq(filterCond)
                )
                .orderBy(product.startDate.asc())
                .fetch();
    }

    @Override
    public Page<ProductFilter.ProductFilterForAdminResponseDto> searchForAdmin(ProductFilter.ProductFilterCond filterCond, Pageable pageable) {
        List<ProductFilter.ProductFilterForAdminResponseDto> productList = jpaQueryFactory
                .select(new QProductFilter_ProductFilterForAdminResponseDto(
                        product.id,
                        product.aPackage.packageName,
                        product.productCode,
                        product.startDate,
                        product.endDate,
                        product.minCount,
                        product.maxCount,
                        product.nowCount,
                        product.productState,
                        product.saveState,
                        product.isPublic
                ))
                .from(product)
                .where(
                        packageIdEq(filterCond),
                        productStateEq(filterCond),
                        privacyEq(filterCond),
                        saveStateEq(filterCond),
                        startDateBtw(filterCond)
                )
                .orderBy(
                        createOrderSpecifier(filterCond)
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
                        privacyEq(filterCond),
                        saveStateEq(filterCond),
                        startDateBtw(filterCond)
                )
                .fetchOne();

        return new PageImpl<>(productList, pageable, count);
    }

    private BooleanExpression packageIdEq(ProductFilter.ProductFilterCond filterCond) {

        if (filterCond.getPackageId() == null)  return null;
        return product.aPackage.id.eq(filterCond.getPackageId());
    }

    private BooleanExpression productStateEq(ProductFilter.ProductFilterCond filterCond) {

        if (filterCond.getProductState() == null)  return null;
        return product.productState.eq(ProductState.from(filterCond.getProductState()));
    }

    private BooleanExpression startDateBtw(ProductFilter.ProductFilterCond filterCond) {

        if (filterCond.getStartDateMax() == null || filterCond.getStartDateMin() == null)  return null;
        return product.startDate.between(filterCond.getStartDateMin().atStartOfDay(), filterCond.getStartDateMax().atTime(LocalTime.MAX));

    }

    private BooleanExpression startDateAfter() {
        return product.startDate.after(LocalDateTime.now());
    }

    private BooleanExpression privacyEq(ProductFilter.ProductFilterCond filterCond) {
        if (filterCond.getPrivacy() == null)  return null;
        return product.isPublic.eq(Release.from(filterCond.getPrivacy()));
    }

    private BooleanExpression saveStateEq(ProductFilter.ProductFilterCond filterCond) {
        if (filterCond.getSaveState() == null)  return null;
        return product.saveState.eq(SaveState.from(filterCond.getSaveState()));
    }

    private OrderSpecifier[] createOrderSpecifier(ProductFilter.ProductFilterCond filterCond) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (filterCond.getStartDateOrder() != null) {
            orderSpecifiers.add(new OrderSpecifier(filterCond.getStartDateOrder() == 0 ? Order.ASC : Order.DESC, product.startDate));
        }

        if (filterCond.getEndDateOrder() != null) {
            orderSpecifiers.add(new OrderSpecifier(filterCond.getEndDateOrder() == 0 ? Order.ASC : Order.DESC, product.endDate));
        }
//
//        if (filterCond.getPrivacyOrder() != null) {
//            orderSpecifiers.add(new OrderSpecifier(filterCond.getPrivacyOrder() == 0 ? Order.ASC : Order.DESC, product.isPublic));
//        }
//
//        if (filterCond.getSaveStateOrder() != null) {
//            orderSpecifiers.add(new OrderSpecifier(filterCond.getSaveStateOrder() == 0 ? Order.ASC : Order.DESC, product.saveState));
//        }
//
//        if (filterCond.getProductStateOrder() != null) {
//            orderSpecifiers.add(new OrderSpecifier(filterCond.getProductStateOrder() == 0 ? Order.ASC : Order.DESC, product.productState));
//        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }
}
