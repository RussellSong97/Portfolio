package com.cuv.domain.memberadmin;

import com.cuv.common.YN;
import com.cuv.domain.member.enumset.MemberRole;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.cuv.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class MemberAdminRepositoryImpl implements MemberAdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberAdminRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long searchMemberDealerOrderByAssignmentAt(String role) {
        return queryFactory
                .select(memberAdmin.id)
                .from(memberAdmin)
                .where(
                        condRoleEq(role),
                        condDelYnEqN(),
                        condUseYnEqY()
                )
                .orderBy(memberAdmin.assignmentAt.asc())
                .fetchFirst();
    }

    private Predicate condRoleEq(String role) {
        return hasText(role) ? memberAdmin.role.eq(MemberRole.ofRole(role)) : null;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(memberAdmin.delYn.eq(YN.N));
    }

    private Predicate condUseYnEqY() {
        return new BooleanBuilder().and(memberAdmin.useYn.eq(YN.Y));
    }

}
