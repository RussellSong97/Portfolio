package com.cuv.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 2020046262L;

    public static final QProduct product = new QProduct("product");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final EnumPath<com.cuv.common.YN> bestValueRecommendYn = createEnum("bestValueRecommendYn", com.cuv.common.YN.class);

    public final NumberPath<Long> carAmountSale = createNumber("carAmountSale", Long.class);

    public final StringPath carColor = createString("carColor");

    public final StringPath carFuel = createString("carFuel");

    public final ListPath<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>> carImageUrl = this.<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>>createList("carImageUrl", com.cuv.domain.attachment.dto.AttachmentDto.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath carMission = createString("carMission");

    public final StringPath carPlateNumber = createString("carPlateNumber");

    public final StringPath carRegDay = createString("carRegDay");

    public final StringPath carRegYear = createString("carRegYear");

    public final EnumPath<com.cuv.common.YN> cartongryeongRecommendYn = createEnum("cartongryeongRecommendYn", com.cuv.common.YN.class);

    public final NumberPath<Long> carUseKm = createNumber("carUseKm", Long.class);

    public final StringPath city = createString("city");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> detailGradeNumber = createNumber("detailGradeNumber", Long.class);

    public final NumberPath<Long> detailModelNumber = createNumber("detailModelNumber", Long.class);

    public final EnumPath<com.cuv.domain.product.enumset.ExteriorShape> extShape = createEnum("extShape", com.cuv.domain.product.enumset.ExteriorShape.class);

    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> makerNumber = createNumber("makerNumber", Long.class);

    public final NumberPath<Long> memberDealerId = createNumber("memberDealerId", Long.class);

    public final NumberPath<Long> modelNumber = createNumber("modelNumber", Long.class);

    public final StringPath performanceInspectionUrl = createString("performanceInspectionUrl");

    public final EnumPath<com.cuv.domain.product.enumset.PostStatus> postStatus = createEnum("postStatus", com.cuv.domain.product.enumset.PostStatus.class);

    public final StringPath postStopReason = createString("postStopReason");

    public final StringPath productUniqueNumber = createString("productUniqueNumber");

    public final EnumPath<com.cuv.common.YN> recommendYn = createEnum("recommendYn", com.cuv.common.YN.class);

    public final StringPath shopName = createString("shopName");

    public final DateTimePath<java.time.LocalDateTime> soldOutAt = createDateTime("soldOutAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final StringPath vehicleIdentificationNumber = createString("vehicleIdentificationNumber");

    public QProduct(String variable) {
        super(Product.class, forVariable(variable));
    }

    public QProduct(Path<? extends Product> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProduct(PathMetadata metadata) {
        super(Product.class, metadata);
    }

}

