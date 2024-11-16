package com.cuv.domain.membercredentials;

import com.cuv.domain.membercredentials.entity.MemberCredentials;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public class MemberCredentialsRepositoryImpl implements MemberCredentialsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MemberCredentialsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
