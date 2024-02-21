package com.uriel.travel.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uriel.travel.domain.*;
import com.uriel.travel.dto.filterCond.PackageFilter;
import com.uriel.travel.dto.filterCond.QPackageFilter_PackageFilterForAdminResponseDto;
import com.uriel.travel.dto.filterCond.QPackageFilter_PackageFilterResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PackageRepositoryCustomImpl implements PackageRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QPackage aPackage = QPackage.package$;
    QTagging tagging = QTagging.tagging;

    @Override
    public Page<PackageFilter.PackageFilterResponseDto> searchPackageByFilter(PackageFilter.PackageFilterCond filterCond, Pageable pageable) {
        List<PackageFilter.PackageFilterResponseDto> packageList = jpaQueryFactory
                .select(new QPackageFilter_PackageFilterResponseDto(
                        aPackage.id,
                        aPackage.packageName,
                        aPackage.summary,
                        aPackage.period,
                        aPackage.country,
                        aPackage.hashTag
                ))
                .from(aPackage)
                .where(aPackage.isPublic.eq(Release.PUBLIC)
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(themeIn(filterCond))))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(familyIn(filterCond))))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(seasonIn(filterCond))))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(priceIn(filterCond)))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(aPackage.count())
                .from(aPackage)
                .where(aPackage.isPublic.eq(Release.PUBLIC)
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(themeIn(filterCond))))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(familyIn(filterCond))))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(seasonIn(filterCond))))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(priceIn(filterCond)))))
                .fetchOne();

        return new PageImpl<>(packageList, pageable, count);
    }

    @Override
    public Page<PackageFilter.PackageFilterForAdminResponseDto> searchByCountryForAdmin(PackageFilter.PackageFilterCondForAdmin filterCond, Pageable pageable) {
        List<PackageFilter.PackageFilterForAdminResponseDto> packageList = jpaQueryFactory
                .select(new QPackageFilter_PackageFilterForAdminResponseDto(
                        aPackage.id,
                        aPackage.packageName,
                        aPackage.country,
                        aPackage.period
                ))
                .from(aPackage)
                .where(
                        countryEq(filterCond)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(aPackage.count())
                .from(aPackage)
                .where(
                        countryEq(filterCond)
                )
                .fetchOne();

        return new PageImpl<>(packageList, pageable, count);
    }

    public BooleanExpression countryEq(PackageFilter.PackageFilterCondForAdmin filterCond) {
        if (filterCond.getCountryName() != null) {
            return aPackage.country.eq(Country.from(filterCond.getCountryName()));
        }
        return null;
    }

    public BooleanExpression themeIn(PackageFilter.PackageFilterCond filterCond) {
        if (!filterCond.getThemeList().isEmpty()) {
            return tagging.tagType.eq(TagType.THEME).and(tagging.tag.id.in(filterCond.getThemeList()));
        }
        return null;
    }

    public BooleanExpression familyIn(PackageFilter.PackageFilterCond filterCond) {
        if (!filterCond.getFamilyList().isEmpty()) {
            return tagging.tagType.eq(TagType.FAMILY).and(tagging.tag.id.in(filterCond.getFamilyList()));
        }
        return null;
    }

    public BooleanExpression seasonIn(PackageFilter.PackageFilterCond filterCond) {
        if (!filterCond.getSeasonList().isEmpty()) {
            return tagging.tagType.eq(TagType.SEASON).and(tagging.tag.id.in(filterCond.getSeasonList()));
        }
        return null;
    }

    public BooleanExpression priceIn(PackageFilter.PackageFilterCond filterCond) {
        if (!filterCond.getPriceList().isEmpty()) {
            return tagging.tagType.eq(TagType.PRICE).and(tagging.tag.id.in(filterCond.getPriceList()));
        }
        return null;
    }

}