package com.cuv.admin.domain.memberadminmemo;

import com.cuv.admin.domain.member.dto.MemberAdminMemoListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cuv.admin.domain.memberadminmemo.entity.QMemberAdminMemo.memberAdminMemo;

public class MemberAdminMemoRepositoryImpl implements MemberAdminMemoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MemberAdminMemoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberAdminMemoListDto> searchMemberAdminMemoList(Long id) {
        return queryFactory
                .select(Projections.fields(MemberAdminMemoListDto.class,
                        memberAdminMemo.id,
                        memberAdminMemo.content,
                        memberAdminMemo.createdAt
                ))
                .from(memberAdminMemo)
                .where(memberAdminMemo.memberId.eq(id))
                .fetch();
    }
}
