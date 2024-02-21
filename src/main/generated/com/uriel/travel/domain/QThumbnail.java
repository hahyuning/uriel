package com.uriel.travel.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QThumbnail is a Querydsl query type for Thumbnail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QThumbnail extends EntityPathBase<Thumbnail> {

    private static final long serialVersionUID = 853588718L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QThumbnail thumbnail = new QThumbnail("thumbnail");

    public final QPackage aPackage;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final StringPath imageUrl = createString("imageUrl");

    public final StringPath originalImageName = createString("originalImageName");

    public final StringPath uploadImageName = createString("uploadImageName");

    public QThumbnail(String variable) {
        this(Thumbnail.class, forVariable(variable), INITS);
    }

    public QThumbnail(Path<? extends Thumbnail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QThumbnail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QThumbnail(PathMetadata metadata, PathInits inits) {
        this(Thumbnail.class, metadata, inits);
    }

    public QThumbnail(Class<? extends Thumbnail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.aPackage = inits.isInitialized("aPackage") ? new QPackage(forProperty("aPackage")) : null;
    }

}

