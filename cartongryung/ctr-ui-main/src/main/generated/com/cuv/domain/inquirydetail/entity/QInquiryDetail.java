package com.cuv.domain.inquirydetail.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInquiryDetail is a Querydsl query type for InquiryDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInquiryDetail extends EntityPathBase<InquiryDetail> {

    private static final long serialVersionUID = 1925153270L;

    public static final QInquiryDetail inquiryDetail = new QInquiryDetail("inquiryDetail");

    public final com.cuv.common.QBaseEntity _super = new com.cuv.common.QBaseEntity(this);

    public final EnumPath<com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus> consultationStatus = createEnum("consultationStatus", com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus.class);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdId = _super.createdId;

    //inherited
    public final EnumPath<com.cuv.common.YN> delYn = _super.delYn;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> inquiryId = createNumber("inquiryId", Long.class);

    public final NumberPath<Long> memberAdminId = createNumber("memberAdminId", Long.class);

    public final EnumPath<com.cuv.domain.inquirydetail.enumset.TradeType> tradeTypeCode = createEnum("tradeTypeCode", com.cuv.domain.inquirydetail.enumset.TradeType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedId = _super.updatedId;

    public QInquiryDetail(String variable) {
        super(InquiryDetail.class, forVariable(variable));
    }

    public QInquiryDetail(Path<? extends InquiryDetail> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInquiryDetail(PathMetadata metadata) {
        super(InquiryDetail.class, metadata);
    }

}

