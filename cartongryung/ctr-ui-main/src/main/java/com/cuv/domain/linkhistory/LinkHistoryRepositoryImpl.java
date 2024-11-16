package com.cuv.domain.linkhistory;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import javax.swing.text.html.parser.Entity;

public class LinkHistoryRepositoryImpl implements LinkHistoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public LinkHistoryRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
}
