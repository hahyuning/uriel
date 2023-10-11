package com.uriel.travel.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.uriel.travel.dto.QPackageFilterResponseDto is a Querydsl Projection type for PackageFilterResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPackageFilterResponseDto extends ConstructorExpression<PackageFilterResponseDto> {

    private static final long serialVersionUID = -1860314693L;

    public QPackageFilterResponseDto(com.querydsl.core.types.Expression<Long> packageId, com.querydsl.core.types.Expression<String> packageName, com.querydsl.core.types.Expression<String> summary, com.querydsl.core.types.Expression<Integer> period, com.querydsl.core.types.Expression<String> country, com.querydsl.core.types.Expression<Integer> price, com.querydsl.core.types.Expression<String> hashTag) {
        super(PackageFilterResponseDto.class, new Class<?>[]{long.class, String.class, String.class, int.class, String.class, int.class, String.class}, packageId, packageName, summary, period, country, price, hashTag);
    }

}

