package com.cuv.domain.pick.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPick is a Querydsl query type for Pick
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPick extends EntityPathBase<Pick> {

    private static final long serialVersionUID = 2004555890L;

    public static final QPick pick = new QPick("pick");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final EnumPath<com.cuv.common.YN> readYn = createEnum("readYn", com.cuv.common.YN.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QPick(String variable) {
        super(Pick.class, forVariable(variable));
    }

    public QPick(Path<? extends Pick> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPick(PathMetadata metadata) {
        super(Pick.class, metadata);
    }

}

