package com.cuv.domain.saleinquiry.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSaleInquiry is a Querydsl query type for SaleInquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSaleInquiry extends EntityPathBase<SaleInquiry> {

    private static final long serialVersionUID = -1883059978L;

    public static final QSaleInquiry saleInquiry = new QSaleInquiry("saleInquiry");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final ListPath<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>> attachments = this.<com.cuv.domain.attachment.dto.AttachmentDto, SimplePath<com.cuv.domain.attachment.dto.AttachmentDto>>createList("attachments", com.cuv.domain.attachment.dto.AttachmentDto.class, SimplePath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inquiryNumber = createString("inquiryNumber");

    public final NumberPath<Long> memberDealerId = createNumber("memberDealerId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final NumberPath<Long> saleVehicleId = createNumber("saleVehicleId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final DateTimePath<java.time.LocalDateTime> visitReservationAt = createDateTime("visitReservationAt", java.time.LocalDateTime.class);

    public QSaleInquiry(String variable) {
        super(SaleInquiry.class, forVariable(variable));
    }

    public QSaleInquiry(Path<? extends SaleInquiry> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSaleInquiry(PathMetadata metadata) {
        super(SaleInquiry.class, metadata);
    }

}

