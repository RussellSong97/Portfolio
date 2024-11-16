package com.cuv.domain.linklisting.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLinkListing is a Querydsl query type for LinkListing
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLinkListing extends EntityPathBase<LinkListing> {

    private static final long serialVersionUID = 1876526262L;

    public static final QLinkListing linkListing = new QLinkListing("linkListing");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final NumberPath<Long> carAmountSale = createNumber("carAmountSale", Long.class);

    public final NumberPath<Long> carCheckoutNo = createNumber("carCheckoutNo", Long.class);

    public final StringPath carColor = createString("carColor");

    public final StringPath carContent = createString("carContent");

    public final StringPath carFrameNo = createString("carFrameNo");

    public final StringPath carFuel = createString("carFuel");

    public final NumberPath<Long> carGradeDetailNo = createNumber("carGradeDetailNo", Long.class);

    public final NumberPath<Long> carGradeNo = createNumber("carGradeNo", Long.class);

    public final NumberPath<Long> carJesiNo = createNumber("carJesiNo", Long.class);

    public final NumberPath<Long> carMakerNo = createNumber("carMakerNo", Long.class);

    public final StringPath carMission = createString("carMission");

    public final NumberPath<Long> carModelDetailNo = createNumber("carModelDetailNo", Long.class);

    public final NumberPath<Long> carModelNo = createNumber("carModelNo", Long.class);

    public final StringPath carName = createString("carName");

    public final StringPath carPlateNumber = createString("carPlateNumber");

    public final StringPath carRegDay = createString("carRegDay");

    public final StringPath carRegYear = createString("carRegYear");

    public final NumberPath<Long> carTruckTon = createNumber("carTruckTon", Long.class);

    public final NumberPath<Long> carUseKm = createNumber("carUseKm", Long.class);

    public final StringPath checkouturl = createString("checkouturl");

    public final StringPath city = createString("city");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>> imageInfo = this.<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>>createList("imageInfo", com.cuv.domain.attachment.dto.AttachmentDto.class, SimplePath.class, PathInits.DIRECT2);

    public final StringPath lastUpdateDate = createString("lastUpdateDate");

    public final EnumPath<com.cuv.common.YN> listedYn = createEnum("listedYn", com.cuv.common.YN.class);

    public final StringPath shopName = createString("shopName");

    public final StringPath sido = createString("sido");

    public final NumberPath<Long> state = createNumber("state", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final StringPath userHp = createString("userHp");

    public final StringPath userName = createString("userName");

    public QLinkListing(String variable) {
        super(LinkListing.class, forVariable(variable));
    }

    public QLinkListing(Path<? extends LinkListing> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLinkListing(PathMetadata metadata) {
        super(LinkListing.class, metadata);
    }

}

