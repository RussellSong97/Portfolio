package com.cuv.domain.bizgo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBizgo is a Querydsl query type for Bizgo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBizgo extends EntityPathBase<Bizgo> {

    private static final long serialVersionUID = 1450553270L;

    public static final QBizgo bizgo = new QBizgo("bizgo");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final StringPath code = createString("code");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final EnumPath<com.cuv.common.YN> viewYn = createEnum("viewYn", com.cuv.common.YN.class);

    public QBizgo(String variable) {
        super(Bizgo.class, forVariable(variable));
    }

    public QBizgo(Path<? extends Bizgo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBizgo(PathMetadata metadata) {
        super(Bizgo.class, metadata);
    }

}

