package com.cuv.domain.linkhistory;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

import static com.cuv.domain.linkhistory.entity.QLinkHistory.linkHistory;

public class LinkHistoryRepositoryImpl implements LinkHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public LinkHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long searchLinkHistoryCountAtToday(Long dbMemberId) {
        return queryFactory
                .select(Wildcard.count)
                .from(linkHistory)
                .where(
                        linkHistory.memberId.eq(dbMemberId)
                                .and(linkHistory.createdAt.goe(LocalDate.now().atStartOfDay()))
                )
                .fetchOne();
    }
}
