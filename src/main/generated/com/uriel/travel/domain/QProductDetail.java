package com.uriel.travel.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductDetail is a Querydsl query type for ProductDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductDetail extends EntityPathBase<ProductDetail> {

    private static final long serialVersionUID = -1003570398L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductDetail productDetail = new QProductDetail("productDetail");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Integer> adultPrice = createNumber("adultPrice", Integer.class);

    public final NumberPath<Integer> adultSurcharge = createNumber("adultSurcharge", Integer.class);

    public final NumberPath<Integer> childPrice = createNumber("childPrice", Integer.class);

    public final NumberPath<Integer> childSurcharge = createNumber("childSurcharge", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath excludedProduct = createString("excludedProduct");

    public final NumberPath<Integer> flagCarrier = createNumber("flagCarrier", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath includedProduct = createString("includedProduct");

    public final NumberPath<Integer> infantPrice = createNumber("infantPrice", Integer.class);

    public final NumberPath<Integer> infantSurcharge = createNumber("infantSurcharge", Integer.class);

    public final StringPath keyPoint = createString("keyPoint");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Integer> optionalTour = createNumber("optionalTour", Integer.class);

    public final QProduct product;

    public final StringPath reservationNotice = createString("reservationNotice");

    public final NumberPath<Integer> shopping = createNumber("shopping", Integer.class);

    public final NumberPath<Integer> tourConductor = createNumber("tourConductor", Integer.class);

    public final StringPath travelRegion = createString("travelRegion");

    public QProductDetail(String variable) {
        this(ProductDetail.class, forVariable(variable), INITS);
    }

    public QProductDetail(Path<? extends ProductDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductDetail(PathMetadata metadata, PathInits inits) {
        this(ProductDetail.class, metadata, inits);
    }

    public QProductDetail(Class<? extends ProductDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

