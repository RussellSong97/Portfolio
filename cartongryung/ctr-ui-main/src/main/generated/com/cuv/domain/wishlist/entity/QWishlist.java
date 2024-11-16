package com.cuv.domain.wishlist.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWishlist is a Querydsl query type for Wishlist
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWishlist extends EntityPathBase<Wishlist> {

    private static final long serialVersionUID = 1741425082L;

    public static final QWishlist wishlist = new QWishlist("wishlist");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final NumberPath<Long> purchaseInquiryId = createNumber("purchaseInquiryId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QWishlist(String variable) {
        super(Wishlist.class, forVariable(variable));
    }

    public QWishlist(Path<? extends Wishlist> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWishlist(PathMetadata metadata) {
        super(Wishlist.class, metadata);
    }

}

