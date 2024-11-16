package com.cuv.domain.purchaseinquiry.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPurchaseInquiry is a Querydsl query type for PurchaseInquiry
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseInquiry extends EntityPathBase<PurchaseInquiry> {

    private static final long serialVersionUID = 374420918L;

    public static final QPurchaseInquiry purchaseInquiry = new QPurchaseInquiry("purchaseInquiry");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final StringPath connectionIp = createString("connectionIp");

    public final EnumPath<com.cuv.domain.purchaseinquiry.enumset.ConsultationType> consultationTypeCode = createEnum("consultationTypeCode", com.cuv.domain.purchaseinquiry.enumset.ConsultationType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inquiryNumber = createString("inquiryNumber");

    public final EnumPath<com.cuv.domain.purchaseinquiry.enumset.InquiryType> inquiryTypeCode = createEnum("inquiryTypeCode", com.cuv.domain.purchaseinquiry.enumset.InquiryType.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath mobileNumber = createString("mobileNumber");

    public final StringPath realName = createString("realName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public final DateTimePath<java.time.LocalDateTime> visitReservationAt = createDateTime("visitReservationAt", java.time.LocalDateTime.class);

    public QPurchaseInquiry(String variable) {
        super(PurchaseInquiry.class, forVariable(variable));
    }

    public QPurchaseInquiry(Path<? extends PurchaseInquiry> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPurchaseInquiry(PathMetadata metadata) {
        super(PurchaseInquiry.class, metadata);
    }

}

