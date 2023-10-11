package com.uriel.travel.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPackage is a Querydsl query type for Package
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPackage extends EntityPathBase<Package> {

    private static final long serialVersionUID = 28194504L;

    public static final QPackage package$ = new QPackage("package$");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final StringPath country = createString("country");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath hashTag = createString("hashTag");

    public final StringPath hotelInfo = createString("hotelInfo");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath packageName = createString("packageName");

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<Product, QProduct> productList = this.<Product, QProduct>createList("productList", Product.class, QProduct.class, PathInits.DIRECT2);

    public final StringPath regionInfo = createString("regionInfo");

    public final ListPath<Schedule, QSchedule> scheduleList = this.<Schedule, QSchedule>createList("scheduleList", Schedule.class, QSchedule.class, PathInits.DIRECT2);

    public final StringPath summary = createString("summary");

    public final StringPath terms = createString("terms");

    public final ListPath<Thumbnail, QThumbnail> thumbnailList = this.<Thumbnail, QThumbnail>createList("thumbnailList", Thumbnail.class, QThumbnail.class, PathInits.DIRECT2);

    public QPackage(String variable) {
        super(Package.class, forVariable(variable));
    }

    public QPackage(Path<? extends Package> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPackage(PathMetadata metadata) {
        super(Package.class, metadata);
    }

}

