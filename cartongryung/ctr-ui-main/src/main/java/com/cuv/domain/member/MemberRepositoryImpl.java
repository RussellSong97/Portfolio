package com.cuv.domain.member;

import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.member.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import static com.cuv.domain.member.entity.QMember.member;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public MemberInfoDto searchMemberInfo(String username) {
        return queryFactory.select(Projections.fields(MemberInfoDto.class,
                member.email,
                member.mobileNumber,
                member.realName,
                member.regCode
                ))
                .from(member)
                .where(member.email.eq(username))
                .fetchOne();
    }
}
