package com.uriel.travel.jwt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRefreshToken is a Querydsl query type for RefreshToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRefreshToken extends EntityPathBase<RefreshToken> {

    private static final long serialVersionUID = 1860091958L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRefreshToken refreshToken1 = new QRefreshToken("refreshToken1");

    public final NumberPath<Long> expiredTime = createNumber("expiredTime", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final com.uriel.travel.domain.QUsers user;

    public QRefreshToken(String variable) {
        this(RefreshToken.class, forVariable(variable), INITS);
    }

    public QRefreshToken(Path<? extends RefreshToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRefreshToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRefreshToken(PathMetadata metadata, PathInits inits) {
        this(RefreshToken.class, metadata, inits);
    }

    public QRefreshToken(Class<? extends RefreshToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.uriel.travel.domain.QUsers(forProperty("user"), inits.get("user")) : null;
    }

}

