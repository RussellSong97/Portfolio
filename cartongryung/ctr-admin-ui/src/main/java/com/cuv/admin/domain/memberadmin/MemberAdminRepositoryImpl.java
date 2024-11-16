package com.cuv.admin.domain.memberadmin;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminDetailDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminSearchDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class MemberAdminRepositoryImpl implements MemberAdminRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberAdminRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberAdminListDto> searchAllMemberAdmin(MemberAdminSearchDto condition, Pageable pageable) {
        List<MemberAdminListDto> content = queryFactory
                .select(Projections.fields(MemberAdminListDto.class,
                        memberAdmin.id,
                        memberAdmin.loginId,
                        memberAdmin.realName,
                        memberAdmin.mobileNumber,
                        memberAdmin.useYn,
                        memberAdmin.lastLoginAt
                        ))
                .from(memberAdmin)
                .where(
                        condRoleEq(condition.getRole()),
                        condDelYnEqN()
                )
                .orderBy(getSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = queryFactory
                .select(Wildcard.count)
                .from(memberAdmin)
                .where(
                        condRoleEq(condition.getRole()),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    @Override
    public MemberAdminDetailDto searchMemberAdminById(Long id) {
        return queryFactory
                .select(Projections.fields(MemberAdminDetailDto.class,
                        memberAdmin.id,
                        memberAdmin.loginId,
                        memberAdmin.realName,
                        memberAdmin.mobileNumber,
                        memberAdmin.employeeNumber,
                        memberAdmin.profileImageJson,
                        memberAdmin.intro,
                        memberAdmin.useYn,
                        memberAdmin.lastLoginAt
                        ))
                .from(memberAdmin)
                .where(
                        condIdEq(id),
                        condDelYnEqN()
                )
                .fetchFirst();
    }

    @Override
    public List<MemberAdminListDto> searchAllMemberDealer(String role) {
        return queryFactory
                .select(Projections.fields(MemberAdminListDto.class,
                        memberAdmin.id,
                        memberAdmin.realName
                        ))
                .from(memberAdmin)
                .where(
                        condRoleEq(role),
                        condDelYnEqN(),
                        condUseYnEqY()
                )
                .orderBy(memberAdmin.realName.asc())
                .fetch();
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

    @Override
    public List<MemberAdminListDto> searchAllMemberDealerAndMemberCounselor(String role) {
        return queryFactory
                .select(Projections.fields(MemberAdminListDto.class,
                        memberAdmin.id,
                        memberAdmin.realName
                ))
                .from(memberAdmin)
                .where(
                        condRoleNe(role),
                        condDelYnEqN(),
                        condUseYnEqY()
                )
                .orderBy(memberAdmin.realName.asc())
                .fetch();
    }

    @Override
    public List<MemberAdminListDto> searchAllMemberCounselor(String role) {
        return queryFactory
                .select(Projections.fields(MemberAdminListDto.class,
                        memberAdmin.id,
                        memberAdmin.realName
                ))
                .from(memberAdmin)
                .where(
                        condRoleEq(role),
                        condDelYnEqN(),
                        condUseYnEqY()
                )
                .orderBy(memberAdmin.realName.asc())
                .fetch();
    }


    /* === === === === === === === === === === */


    private Predicate condIdEq(Long id) {
        return id != null ? memberAdmin.id.eq(id) : null;
    }

    private Predicate condRoleEq(String role) {
        return hasText(role) ? memberAdmin.role.eq(MemberRole.ofRole(role)) : null;
    }

    private Predicate condRoleNe(String role) {
        return hasText(role) ? memberAdmin.role.ne(MemberRole.ofRole(role)) : null;
    }

    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(memberAdmin.delYn.eq(YN.N));
    }

    private Predicate condUseYnEqY() {
        return new BooleanBuilder().and(memberAdmin.useYn.eq(YN.Y));
    }


    /* === === === === === === === === === === */


    private OrderSpecifier<?>[] getSort(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (pageable.getSort().isEmpty())
            return new OrderSpecifier[]{memberAdmin.id.desc()};

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            if (order.getProperty().equals("id")) {
                orderSpecifiers.add(new OrderSpecifier<>(direction, memberAdmin.id));
            }
        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

}
