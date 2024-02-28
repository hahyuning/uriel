package com.uriel.travel.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uriel.travel.domain.Country;
import com.uriel.travel.domain.Release;
import com.uriel.travel.domain.SaveState;
import com.uriel.travel.domain.TagType;
import com.uriel.travel.domain.dto.filterCond.PackageFilter;
import com.uriel.travel.domain.dto.filterCond.QPackageFilter_PackageFilterForAdminResponseDto;
import com.uriel.travel.domain.dto.filterCond.QPackageFilter_PackageFilterResponseDto;
import com.uriel.travel.domain.entity.QPackage;
import com.uriel.travel.domain.entity.QTagging;
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
    public List<PackageFilter.PackageFilterResponseDto> searchPackageByFilter(PackageFilter.PackageFilterCond filterCond) {
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

        return packageList;
    }

    @Override
    public Page<PackageFilter.PackageFilterForAdminResponseDto> searchPackageForAdmin(PackageFilter.PackageFilterCondForAdmin filterCond, Pageable pageable) {
        List<PackageFilter.PackageFilterForAdminResponseDto> packageList = jpaQueryFactory
                .select(new QPackageFilter_PackageFilterForAdminResponseDto(
                        aPackage.id,
                        aPackage.packageName,
                        aPackage.country,
                        aPackage.period,
                        aPackage.saveState,
                        aPackage.isPublic
                ))
                .from(aPackage)
                .where(
                        countryEq(filterCond),
                        privacyEq(filterCond),
                        saveStateEq(filterCond)
                )
                .orderBy(
//                        countryOder(filterCond),
                        periodOrder(filterCond)
//                        saveStateOrder(filterCond),
//                        privacyOrder(filterCond)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(aPackage.count())
                .from(aPackage)
                .where(
                        countryEq(filterCond),
                        privacyEq(filterCond),
                        saveStateEq(filterCond)
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

    public BooleanExpression privacyEq(PackageFilter.PackageFilterCondForAdmin filterCond) {
        if (filterCond.getPrivacy() != null) {
            return aPackage.isPublic.eq(Release.from(filterCond.getPrivacy()));
        }
        return null;
    }

    public BooleanExpression saveStateEq(PackageFilter.PackageFilterCondForAdmin filterCond) {
        if (filterCond.getSaveState() != null) {
            return aPackage.saveState.eq(SaveState.from(filterCond.getSaveState()));
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

//    public OrderSpecifier<Country> countryOder(PackageFilter.PackageFilterCondForAdmin filterCond) {
//        if (filterCond.getCountryOrder() != null) {
//            if (filterCond.getCountryOrder() == 0) {
//                return aPackage.country.asc();
//            } else {
//                return aPackage.country.desc();
//            }
//        }
//        return aPackage.country.asc();
//    }

    public OrderSpecifier<Integer> periodOrder(PackageFilter.PackageFilterCondForAdmin filterCond) {
        if (filterCond.getPeriodOrder() != null) {
            if (filterCond.getPeriodOrder() == 0) {
                return aPackage.period.asc();
            } else {
                return aPackage.period.desc();
            }
        }
        return aPackage.period.asc();
    }

//    public OrderSpecifier<SaveState> saveStateOrder(PackageFilter.PackageFilterCondForAdmin filterCond) {
//        if (filterCond.getSaveStateOrder() != null) {
//            if (filterCond.getSaveStateOrder() == 0) {
//                return aPackage.saveState.asc();
//            } else {
//                return aPackage.saveState.desc();
//            }
//        }
//        return aPackage.saveState.asc();
//    }
//
//    public OrderSpecifier<Release> privacyOrder(PackageFilter.PackageFilterCondForAdmin filterCond) {
//        if (filterCond.getPrivacyOrder() != null) {
//            if (filterCond.getPrivacyOrder() == 0) {
//                return aPackage.isPublic.asc();
//            } else {
//                return aPackage.isPublic.desc();
//            }
//        }
//        return aPackage.isPublic.asc();
//    }
}