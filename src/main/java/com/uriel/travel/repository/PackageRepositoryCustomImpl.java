package com.uriel.travel.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.uriel.travel.domain.QPackage;
import com.uriel.travel.domain.QTagging;
import com.uriel.travel.domain.TagType;
import com.uriel.travel.dto.PackageFilterResponseDto;
import com.uriel.travel.dto.PackageRequestDto;
import com.uriel.travel.dto.QPackageFilterResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<PackageFilterResponseDto> searchPackageByFilter(PackageRequestDto.FilterCond filterCond) {
        return jpaQueryFactory
                .select(new QPackageFilterResponseDto(
                        aPackage.id,
                        aPackage.packageName,
                        aPackage.summary,
                        aPackage.period,
                        aPackage.country,
                        aPackage.price,
                        aPackage.hashTag
                ))
                .from(aPackage)
                .where(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(themeIn(filterCond)))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(familyIn(filterCond))))
                        .and(aPackage.id.in(JPAExpressions.select(tagging.aPackage.id).from(tagging).where(seasonIn(filterCond))))
                        .and(costLoe(filterCond)))
                .fetch();
    }

    public BooleanExpression themeIn(PackageRequestDto.FilterCond filterCond) {
        if (!filterCond.getThemeList().isEmpty()) {
            return tagging.tagType.eq(TagType.THEME).and(tagging.tag.id.in(filterCond.getThemeList()));
        }
        return null;
    }

    public BooleanExpression familyIn(PackageRequestDto.FilterCond filterCond) {
        if (!filterCond.getFamilyList().isEmpty()) {
            return tagging.tagType.eq(TagType.FAMILY).and(tagging.tag.id.in(filterCond.getFamilyList()));
        }
        return null;
    }

    public BooleanExpression seasonIn(PackageRequestDto.FilterCond filterCond) {
        if (!filterCond.getSeasonList().isEmpty()) {
            return tagging.tagType.eq(TagType.SEASON).and(tagging.tag.id.in(filterCond.getSeasonList()));
        }
        return null;
    }

    public BooleanExpression costLoe(PackageRequestDto.FilterCond filterCond) {
        return aPackage.price.loe(filterCond.getCost());
    }
}