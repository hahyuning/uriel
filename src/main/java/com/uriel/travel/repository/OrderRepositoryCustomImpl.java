package com.uriel.travel.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.OrderState;
import com.uriel.travel.domain.SearchType;
import com.uriel.travel.domain.dto.order.OrderFilter;
import com.uriel.travel.domain.dto.order.QOrderFilter_OrderFilterResponseDto;
import com.uriel.travel.domain.dto.order.QOrderFilter_OrderFilterResponseDtoForAdmin;
import com.uriel.travel.domain.entity.QOrder;
import com.uriel.travel.domain.entity.QProduct;
import com.uriel.travel.domain.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QOrder order = QOrder.order;
    QProduct product = QProduct.product;
    QUser user = QUser.user;

    @Override
    public List<OrderFilter.OrderFilterResponseDto> findByReserveUser(OrderFilter.OrderFilterCond filterCond, String reserveUserEmail, Pageable pageable) {
        return jpaQueryFactory
                .select(new QOrderFilter_OrderFilterResponseDto(
                        order.imomOrderId,
                        order.orderDate,
                        order.orderState,
                        product.aPackage.packageName,
                        product.startDate,
                        order.totalCount,
                        product.productState
                ))
                .from(order)
                .join(product)
                .on(order.productId.eq(product.id))
                .where(
                        reserveUserEq(reserveUserEmail)
                )
                .orderBy(
                        createOrderSpecifier(filterCond)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

//    @Override
//    public Page<OrderFilter.OrderFilterResponseDtoForAdmin> searchOrder(OrderFilter.OrderSearchCond searchCond, Pageable pageable) {
//        List<OrderFilter.OrderFilterResponseDtoForAdmin> orderList = jpaQueryFactory
//                .select(new QOrderFilter_OrderFilterResponseDtoForAdmin(
//                        order.imomOrderId,
//                        order.orderDate,
//                        order.orderState,
//                        order.product.aPackage.packageName,
//                        order.product.productCode,
//                        order.product.productState,
//                        order.product.startDate,
//                        order.reserveUser.krName,
//                        order.reserveUser.phoneNumber,
//                        order.reserveUser.email,
//                        order.totalCount
//                ))
//                .from(order)
//                .where(
//                        searchTarget(searchCond)
//                )
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Long count = jpaQueryFactory
//                .select(order.count())
//                .from(order)
//                .where(
//                        searchTarget(searchCond)
//                )
//                .fetchOne();
//
//        return new PageImpl<>(orderList, pageable, count);
//    }

    @Override
    public Page<OrderFilter.OrderFilterResponseDtoForAdmin> ordersByFilter(OrderFilter.OrderFilterCond filterCond, Pageable pageable) {
        List<OrderFilter.OrderFilterResponseDtoForAdmin> orderList = jpaQueryFactory
                .select(new QOrderFilter_OrderFilterResponseDtoForAdmin(
                        order.imomOrderId,
                        order.orderDate,
                        order.orderState,
                        product.aPackage.packageName,
                        product.aPackage.country,
                        product.productCode,
                        product.productState,
                        product.startDate,
                        user.krName,
                        user.phoneNumber,
                        user.email,
                        order.totalCount
                ))
                .from(order)
                .join(product).on(order.productId.eq(product.id))
                .join(user).on(order.reserveUserEmail.eq(user.email))
                .where(
                        packageIdEq(filterCond),
                        countryEq(filterCond),
                        orderStateEq(filterCond),
                        orderDateBtw(filterCond),
                        searchTarget(filterCond)
                )
                .orderBy(
                        createOrderSpecifier(filterCond)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(order.count())
                .from(order)
                .where(
                        packageIdEq(filterCond),
                        countryEq(filterCond),
                        orderStateEq(filterCond),
                        orderDateBtw(filterCond)
                )
                .fetchOne();

        return new PageImpl<>(orderList, pageable, count);
    }

    private BooleanExpression reserveUserEq(String reserveUserEmail) {
        return order.reserveUserEmail.eq(reserveUserEmail);
    }

    private BooleanExpression orderDateBtw(OrderFilter.OrderFilterCond filterCond) {

        if (filterCond.getOrderDateMin() == null || filterCond.getOrderDateMax() == null)  return null;
        return order.orderDate.between(filterCond.getOrderDateMin(), filterCond.getOrderDateMax());

    }

    private BooleanExpression packageIdEq(OrderFilter.OrderFilterCond filterCond) {

        if (filterCond.getPackageId() == null)  return null;
        return product.aPackage.id.eq(filterCond.getPackageId());
    }

    private BooleanExpression orderStateEq(OrderFilter.OrderFilterCond filterCond) {

        if (filterCond.getOrderState() == null)  return null;
        return order.orderState.eq(OrderState.from(filterCond.getOrderState()));
    }

    public BooleanExpression countryEq(OrderFilter.OrderFilterCond filterCond) {
        if (filterCond.getCountry() != null) {
            return product.aPackage.country.eq(Country.from(filterCond.getCountry()));
        }
        return null;
    }

    private OrderSpecifier[] createOrderSpecifier(OrderFilter.OrderFilterCond filterCond) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        if (filterCond.getUserNameOrder() != null) {
            orderSpecifiers.add(new OrderSpecifier(filterCond.getUserNameOrder() == 0 ? Order.ASC : Order.DESC, user.krName));
        }

        if (filterCond.getOrder() != null) {
            orderSpecifiers.add(new OrderSpecifier(filterCond.getOrder() == 0 ? Order.ASC : Order.DESC, order.orderDate));
        }

        if (filterCond.getStart() != null) {
            orderSpecifiers.add(new OrderSpecifier(filterCond.getStart() == 0 ? Order.ASC : Order.DESC, product.startDate));
        }
        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

    public BooleanExpression searchTarget(OrderFilter.OrderFilterCond searchCond) {
        if (searchCond.getType() == null || searchCond.getTarget() == null) {
            return null;
        }

        if (Objects.equals(SearchType.from(searchCond.getType()), SearchType.RESERVE_USER)) {
            return user.krName.eq(searchCond.getTarget());
        } else if (Objects.equals(SearchType.from(searchCond.getType()), SearchType.PHONE_NUMBER)) {
            return user.phoneNumber.eq(searchCond.getTarget());
        } else if (Objects.equals(SearchType.from(searchCond.getType()), SearchType.EMAIL)) {
            return order.reserveUserEmail.eq(searchCond.getTarget());
        } else if (Objects.equals(SearchType.from(searchCond.getType()), SearchType.IMOM_ORDER_ID)) {
            return order.imomOrderId.eq(searchCond.getTarget());
        } else if (Objects.equals(SearchType.from(searchCond.getType()), SearchType.PRODUCT_CODE)) {
            return order.productCode.eq(searchCond.getTarget());
        }

        return null;
    }
}
