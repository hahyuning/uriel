package com.uriel.travel.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVerifiedUser is a Querydsl query type for VerifiedUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVerifiedUser extends EntityPathBase<VerifiedUser> {

    private static final long serialVersionUID = 1986664817L;

    public static final QVerifiedUser verifiedUser = new QVerifiedUser("verifiedUser");

    public final StringPath Email = createString("Email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath UserName = createString("UserName");

    public QVerifiedUser(String variable) {
        super(VerifiedUser.class, forVariable(variable));
    }

    public QVerifiedUser(Path<? extends VerifiedUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVerifiedUser(PathMetadata metadata) {
        super(VerifiedUser.class, metadata);
    }

}

