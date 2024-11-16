package com.cuv.domain.productviewshistory.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProductViewsHistory is a Querydsl query type for ProductViewsHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductViewsHistory extends EntityPathBase<ProductViewsHistory> {

    private static final long serialVersionUID = 736291574L;

    public static final QProductViewsHistory productViewsHistory = new QProductViewsHistory("productViewsHistory");

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

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QProductViewsHistory(String variable) {
        super(ProductViewsHistory.class, forVariable(variable));
    }

    public QProductViewsHistory(Path<? extends ProductViewsHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductViewsHistory(PathMetadata metadata) {
        super(ProductViewsHistory.class, metadata);
    }

}

