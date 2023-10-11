package com.uriel.travel.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTag is a Querydsl query type for Tag
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTag extends EntityPathBase<Tag> {

    private static final long serialVersionUID = -198946020L;

    public static final QTag tag = new QTag("tag");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath tagContent = createString("tagContent");

    public final ListPath<Tagging, QTagging> taggingList = this.<Tagging, QTagging>createList("taggingList", Tagging.class, QTagging.class, PathInits.DIRECT2);

    public final EnumPath<TagType> tagType = createEnum("tagType", TagType.class);

    public QTag(String variable) {
        super(Tag.class, forVariable(variable));
    }

    public QTag(Path<? extends Tag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTag(PathMetadata metadata) {
        super(Tag.class, metadata);
    }

}

