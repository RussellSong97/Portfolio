package com.cuv.domain.saleinquiry;

import com.cuv.common.YN;
import com.cuv.domain.inquirydetail.enumset.TradeType;
import com.cuv.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.domain.saleinquiry.dto.SaleInquiryListDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.inquirydetail.entity.QInquiryDetail.inquiryDetail;
import static com.cuv.domain.linkinfo.entity.QLinkInfo.linkInfo;
import static com.cuv.domain.member.entity.QMember.member;
import static com.cuv.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.domain.saleinquiry.entity.QSaleInquiry.saleInquiry;
import static com.cuv.domain.salevehicle.entity.QSaleVehicle.saleVehicle;

@Repository
public class SaleInquiryRepositoryImpl implements SaleInquiryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SaleInquiryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<SaleInquiryListDto> searchAllSaleInquiryVehicleLists(Long memberId) {
        return queryFactory
                .select(Projections.fields(SaleInquiryListDto.class,
                        saleInquiry.id,
                        saleVehicle.carPlateNumber,
                        saleInquiry.attachments,
                        saleInquiry.createdAt
                ))
                .from(saleInquiry)
                .innerJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .where(
                        condMemberIdEq(memberId),
                        condPaymentYnEqN(),
                        condDelYnEqN()
                )
                .orderBy(saleInquiry.createdAt.desc())
                .fetch();
    }

    @Override
    public SaleInquiryDetailDto searchSaleInquiryById(Long id) {
        return queryFactory
                .select(Projections.fields(SaleInquiryDetailDto.class,
                        saleInquiry.id,
                        saleVehicle.carPlateNumber,
                        saleInquiry.attachments,
                        saleInquiry.createdAt
                ))
                .from(saleInquiry)
                .innerJoin(saleVehicle)
                .on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .where(
                        condIdEq(id)
                )
                .fetchFirst();
    }

    @Override
    public Long searchSaleInquiryCountByMemberId(Long memberId) {
        return queryFactory
                .select(Wildcard.count)
                .from(saleInquiry)
                .where(
                        condMemberIdEq(memberId),
                        condDelYnEqN()
                )
                .fetchOne();
    }

    @Override
    public List<SaleInquiryListDto> searchAllSaleInquiryLists(Long memberId) {
        return queryFactory
                .select(Projections.fields(SaleInquiryListDto.class,
                        ExpressionUtils.as(JPAExpressions
                                        .select(inquiryDetail.consultationStatus)
                                        .from(inquiryDetail)
                                        .where(
                                                inquiryDetail.id.eq(
                                                        JPAExpressions
                                                                .select(inquiryDetail.id.max())
                                                                .from(inquiryDetail)
                                                                .where(
                                                                        inquiryDetail.inquiryId.eq(saleInquiry.id)
                                                                )
                                                ).and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL))
                                        )
                                , "consultationStatus"),
                        memberAdmin.realName,
                        memberAdmin.mobileNumber,
                        memberAdmin.profileImageJson.as("profileImage"),
                        saleInquiry.visitReservationAt
                ))
                .from(saleInquiry)
                .leftJoin(member)
                .on(member.id.eq(saleInquiry.memberId))
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(saleInquiry.memberDealerId))
                .where(
                        condMemberIdEq(memberId)
                )
                .fetch();
    }

    private Predicate condIdEq(Long id) {
        return id != null ? saleInquiry.id.eq(id) : null;
    }

    private Predicate condMemberIdEq(Long id) {
        return id != null ? saleInquiry.memberId.eq(id) : null;
    }

    private Predicate condPaymentYnEqN() {
        return new BooleanBuilder().and(saleVehicle.paymentYn.eq(YN.N));
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(saleInquiry.delYn.eq(YN.N));
    }

}
