package com.cuv.admin.domain.linkcode;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.linkcode.dto.LinkCodeDetailDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.admin.domain.linkcode.dto.LinkCodeSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.admin.domain.linkcode.entity.QLinkCode.linkCode;

@Repository
public class LinkCodeRepositoryImpl implements LinkCodeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LinkCodeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<LinkCodeListDto> searchAllLinkCode(LinkCodeSearchDto condition) {
        return queryFactory
                .select(Projections.fields(LinkCodeListDto.class,
                        linkCode.id,
                        linkCode.parentLinkNbrId,
                        linkCode.linkDataNm,
                        linkCode.depth,
                        linkCode.attachment,
                        linkCode.viewYn
                ))
                .from(linkCode)
                .where(
                        condParentLinkNbrId(condition.getParentLinkNbrId()),
                        condDepthEq(condition.getDepth()),
                        condDelYnEqN()
                )
                .orderBy(linkCode.viewOrder.asc())
                .fetch();
    }

    @Override
    public Integer searchLastOrderSeq(Long parentLinkNbrId, Integer depth) {
        return queryFactory
                .select(
                        linkCode.viewOrder
                )
                .from(linkCode)
                .where(
                        condParentLinkNbrId(parentLinkNbrId),
                        condDepthEq(depth),
                        condDelYnEqN()
                )
                .orderBy(linkCode.viewOrder.desc())
                .fetchFirst();
    }

    @Override
    public LinkCodeDetailDto searchLinkCodeById(Long id) {
        return queryFactory
                .select(Projections.fields(LinkCodeDetailDto.class,
                        linkCode.id,
                        linkCode.linkDataNm,
                        linkCode.depth,
                        linkCode.attachment,
                        linkCode.afterServiceDate,
                        linkCode.viewYn
                ))
                .from(linkCode)
                .where(
                        condIdEq(id)
                )
                .fetchFirst();
    }

    private Predicate condIdEq(Long id) {
        return id != null ? linkCode.id.eq(id) : null;
    }

    private Predicate condParentLinkNbrId(Long upperLinkNbrId) {
        return upperLinkNbrId != null ? linkCode.parentLinkNbrId.eq(upperLinkNbrId) : null;
    }

    private Predicate condDepthEq(Integer depth) {
        return depth != null ? linkCode.depth.eq(depth) : null;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(linkCode.delYn.eq(YN.N));
    }

}
