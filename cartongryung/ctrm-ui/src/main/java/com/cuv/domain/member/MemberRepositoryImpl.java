package com.cuv.domain.member;

import com.cuv.domain.member.dto.MemberInfoDto;
import com.cuv.domain.member.enumset.MemberStatus;
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
    public MemberInfoDto searchMemberInfo(String loginId) {
        return queryFactory.select(Projections.fields(MemberInfoDto.class,
                        member.email,
                        member.mobileNumber,
                        member.realName,
                        member.regCode,
                        member.profileImageJson.as("profileImage"),
                        member.agreeMarketingYn,
                        member.agreeNoticeYn,
                        member.agreePushYn
                ))
                .from(member)
                .where(member.loginId.eq(loginId).and(member.statusCode.eq(MemberStatus.NORMAL)))
                .fetchOne();
    }

    @Override
    public MemberInfoDto searchEmailAndProviderIdAndMemberStatus(String email, String providerId) {
        return queryFactory
                .select(Projections.fields(MemberInfoDto.class,
                        member.statusCode,
                        member.id
                        ))
                .from(member)
                .where(member.email.eq(email)
                        .and(member.providerId.eq(providerId))
                        .and(member.statusCode.eq(MemberStatus.PAUSE)))
                .orderBy(member.memberUpdatedAt.asc())
                .fetchFirst();
    }
}
