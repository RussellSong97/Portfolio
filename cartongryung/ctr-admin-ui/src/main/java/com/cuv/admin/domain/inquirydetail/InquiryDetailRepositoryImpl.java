package com.cuv.admin.domain.inquirydetail;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailListDto;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.admin.domain.inquirydetail.entity.QInquiryDetail.inquiryDetail;
import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class InquiryDetailRepositoryImpl implements InquiryDetailRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public InquiryDetailRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<InquiryDetailListDto> searchAllInquiryDetailListsByTradeType(Long id, String tradeType) {
        return queryFactory
                .select(Projections.fields(InquiryDetailListDto.class,
                        memberAdmin.realName,
                        memberAdmin.role,
                        inquiryDetail.consultationStatus,
                        inquiryDetail.createdAt,
                        inquiryDetail.content
                ))
                .from(inquiryDetail)
                .leftJoin(memberAdmin)
                .on(memberAdmin.id.eq(inquiryDetail.memberAdminId))
                .where(
                        condInquiryIdEq(id),
                        condTradeTypeEq(tradeType),
                        condDelYnEqN()
                )
                .orderBy(inquiryDetail.id.desc())
                .fetch();

    }

    private Predicate condInquiryIdEq(Long id) {
        return id != null ? inquiryDetail.inquiryId.eq(id) : null;
    }

    private Predicate condTradeTypeEq(String tradeType) {
        return hasText(tradeType) ? inquiryDetail.tradeTypeCode.eq(TradeType.ofCode(tradeType)) : null;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(inquiryDetail.delYn.eq(YN.N));
    }

}
