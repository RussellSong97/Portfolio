package com.cuv.domain.popularkeyword.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPopularKeyword is a Querydsl query type for PopularKeyword
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPopularKeyword extends EntityPathBase<PopularKeyword> {

    private static final long serialVersionUID = 288028592L;

    public static final QPopularKeyword popularKeyword = new QPopularKeyword("popularKeyword");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> detailModelCodeId = createNumber("detailModelCodeId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> makerCodeId = createNumber("makerCodeId", Long.class);

    public final NumberPath<Long> modelCodeId = createNumber("modelCodeId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QPopularKeyword(String variable) {
        super(PopularKeyword.class, forVariable(variable));
    }

    public QPopularKeyword(Path<? extends PopularKeyword> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPopularKeyword(PathMetadata metadata) {
        super(PopularKeyword.class, metadata);
    }

}

