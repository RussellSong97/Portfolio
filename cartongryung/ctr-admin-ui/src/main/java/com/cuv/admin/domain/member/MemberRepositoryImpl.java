package com.cuv.admin.domain.member;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.member.dto.*;
import com.cuv.admin.domain.member.enumset.MemberStatus;
import com.cuv.admin.domain.notificatonMember.dto.NotificatonMemberMemberIdAndTokenDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.cuv.admin.domain.member.entity.QMember.member;
import static com.cuv.admin.domain.product.entity.QProduct.product;
import static com.cuv.admin.domain.purchaseinquiry.entity.QPurchaseInquiry.purchaseInquiry;
import static com.cuv.admin.domain.saleinquiry.entity.QSaleInquiry.saleInquiry;
import static com.cuv.admin.domain.salevehicle.entity.QSaleVehicle.saleVehicle;
import static com.cuv.admin.domain.wishlist.entity.QWishlist.wishlist;
import static com.querydsl.core.types.dsl.Expressions.dateTemplate;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<MemberListDto> searchAllMember(MemberSearchDto condition, PageRequest request) {
        List<MemberListDto> content;

        content = queryFactory
                .select(Projections.fields(MemberListDto.class,
                        member.id,
                        member.email,
                        member.realName,
                        member.mobileNumber,
                        member.statusCode,
                        member.regCode,
                        member.lastLoginAt,
                        member.memberCreatedAt,
                        Expressions.as(JPAExpressions.select(Wildcard.count)
                                .from(purchaseInquiry)
                                .where(purchaseInquiry.memberId.eq(member.id).and(purchaseInquiry.delYn.eq(YN.N))), "purchaseCount"),
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(saleInquiry)
                                .where(saleInquiry.memberId.eq(member.id).and(saleInquiry.delYn.eq(YN.N))), "saleCount")
                ))
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()),
                        member.statusCode.ne(MemberStatus.SECESSION)
                )
                .orderBy(getSort(request))
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()),
                        member.statusCode.ne(MemberStatus.SECESSION)
                );

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }

    @Override
    public MemberDetailDto searchMemberById(Long id) {
        MemberDetailDto content = queryFactory
                .select(Projections.fields(MemberDetailDto.class,
                        member.id,
                        member.email,
                        member.realName,
                        member.mobileNumber,
                        member.statusCode,
                        member.regCode,
                        member.lastLoginAt,
                        member.memberCreatedAt,
                        member.agreeMarketingYn,
                        member.profileImageJson.as("profileImage")
                ))
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();

        return content;
    }

    @Override
    public Page<MemberListDto> searchAllMemberWithdrawList(MemberSearchDto condition, PageRequest request) {
        List<MemberListDto> content;

        content = queryFactory
                .select(Projections.fields(MemberListDto.class,
                        member.id,
                        member.email,
                        member.realName,
                        member.mobileNumber,
                        member.statusCode,
                        member.regCode,
                        member.lastLoginAt,
                        member.memberCreatedAt,
                        member.withdrawAt,
                        member.withdrawReason,
                        Expressions.as(JPAExpressions.select(Wildcard.count)
                                .from(purchaseInquiry)
                                .where(purchaseInquiry.memberId.eq(member.id).and(purchaseInquiry.delYn.eq(YN.N))), "purchaseCount"),
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(saleInquiry)
                                .where(saleInquiry.memberId.eq(member.id).and(saleInquiry.delYn.eq(YN.N))), "saleCount")
                ))
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        member.statusCode.eq(MemberStatus.SECESSION)
                )
                .orderBy(getSort(request))
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        member.statusCode.eq(MemberStatus.SECESSION)
                );
        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }

    @Override
    public MemberWithdrawDto searchMemberWithdrawPopup(Long id) {
        return queryFactory.select(Projections.fields(MemberWithdrawDto.class,
                        member.withdrawAt,
                        member.withdrawReason
                ))
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<MemberPurchaseInquiryListDto> searchMemberPurchaseList(Long id) {
        return queryFactory.select(Projections.fields(MemberPurchaseInquiryListDto.class,
                        purchaseInquiry.id,
                        purchaseInquiry.createdAt,
                        ExpressionUtils.as(JPAExpressions
                                        .select(product.carPlateNumber.min())
                                        .from(wishlist)
                                        .innerJoin(product)
                                        .on(product.id.eq(wishlist.productId))
                                        .where(
                                                wishlist.purchaseInquiryId.eq(
                                                        JPAExpressions
                                                                .select(wishlist.purchaseInquiryId.max())
                                                                .from(wishlist)
                                                                .where(
                                                                        wishlist.purchaseInquiryId.eq(purchaseInquiry.id)
                                                                )
                                                )
                                        )
                                , "carPlateNumber"),
                        purchaseInquiry.inquiryNumber
                ))
                .from(purchaseInquiry)
                .where(purchaseInquiry.memberId.eq(id))
                .fetch();
    }

    @Override
    public List<MemberSaleInquiryListDto> searchMemberSaleList(Long id) {
        return queryFactory.select(Projections.fields(MemberSaleInquiryListDto.class,
                        saleInquiry.id,
                        saleInquiry.createdAt,
                        saleInquiry.inquiryNumber,
                        saleVehicle.carPlateNumber
                )).from(saleInquiry)
                .leftJoin(saleVehicle).on(saleVehicle.id.eq(saleInquiry.saleVehicleId))
                .where(saleVehicle.memberId.eq(id))
                .fetch();
    }

    @Override
    public List<MemberJoinCountDto> searchAllWeekJoinMemberCountByMain(String week) {
        DateTemplate<String> dateTemplate = dateTemplate(String.class, "DATE_FORMAT({0}, {1})",
                member.memberCreatedAt, ConstantImpl.create("%Y-%m-%d"));

        return queryFactory
                .select(Projections.fields(MemberJoinCountDto.class,
                        Wildcard.count.as("count"),
                        dateTemplate.as("createdDate")
                ))
                .from(member)
                .where(condCreatedAtBetween(week),
                        member.statusCode.eq(MemberStatus.NORMAL))
                .groupBy(dateTemplate)
                .orderBy(dateTemplate.asc())
                .fetch();
    }

    @Override
    public Long searchAllTodayJoinMemberCountByMain(String today) {
        return queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(condCreatedAtBetween(today),
                        member.statusCode.eq(MemberStatus.NORMAL))
                .fetchOne();
    }

    @Override
    public Long searchAllMemberCountByMain() {
        return queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(
                        member.statusCode.ne(MemberStatus.SECESSION)
                )
                .fetchOne();
    }

    // 활동 중인 회원만 가져오기(키워드, 종류, 가입일-시작, 끝-)
    @Override
    public List<MemberListDto> adminActiveMemberTableExcel(MemberSearchDto condition) {
        return queryFactory
                .select(Projections.fields(MemberListDto.class,
                        member.id,
                        member.realName,
                        member.email,
                        member.mobileNumber,
                        member.memberCreatedAt,
                        member.regCode,
                        member.lastLoginAt,
                        Expressions.as(JPAExpressions.select(Wildcard.count)
                                .from(purchaseInquiry)
                                .where(purchaseInquiry.memberId.eq(member.id).and(purchaseInquiry.delYn.eq(YN.N))), "purchaseCount"),
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(saleInquiry)
                                .where(saleInquiry.memberId.eq(member.id).and(saleInquiry.delYn.eq(YN.N))), "saleCount"),
                        member.statusCode
                ))

                .from(member)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))

                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()),
                        member.statusCode.ne(MemberStatus.SECESSION)
                )
                .orderBy(member.id.desc())
                .fetch();
    }

    // 탈퇴, 탈퇴진행 중인 회원만 가져오기(키워드, 종류, 가입일-시작, 끝-)
    @Override
    public List<MemberListDto> adminWithdrawMemberTableExcel(MemberSearchDto condition) {
        return queryFactory
                .select(Projections.fields(MemberListDto.class,
                        member.id,
                        member.realName,
                        member.email,
                        member.mobileNumber,
                        member.memberCreatedAt,
                        member.regCode,
                        member.lastLoginAt,
                        Expressions.as(JPAExpressions.select(Wildcard.count)
                                .from(purchaseInquiry)
                                .where(purchaseInquiry.memberId.eq(member.id).and(purchaseInquiry.delYn.eq(YN.N))), "purchaseCount"),
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(saleInquiry)
                                .where(saleInquiry.memberId.eq(member.id).and(saleInquiry.delYn.eq(YN.N))), "saleCount"),
                        member.statusCode,
                        member.withdrawAt,
                        member.withdrawReason
                ))

                .from(member)
                .leftJoin(member)
                .on(member.id.eq(purchaseInquiry.memberId))

                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()),
                        member.statusCode.eq(MemberStatus.SECESSION)
                )
                .orderBy(member.id.desc())
                .fetch();
    }

    // 모든 멤버의 토큰과 id 가져오기
    public List<NotificatonMemberMemberIdAndTokenDto> findAllMemberIdAndFcmToken(List<Long> ids) {
        return queryFactory
                .select(Projections.fields(NotificatonMemberMemberIdAndTokenDto.class,
                        member.id.as("memberId"),
                        member.fcmToken.as("token")
                ))
                .from(member)
                .where(
                        member.delYn.eq(YN.N), member.statusCode.eq(MemberStatus.NORMAL),
                        member.fcmToken.isNotNull(), member.agreePushYn.eq(YN.Y),
                        condMemberIdsContain(ids)
                )
                .orderBy(member.id.desc())
                .fetch();
    }

    // 모든 멤버의 토큰과 id 가져오기
    @Override
    public List<NotificatonMemberMemberIdAndTokenDto> findMemberIdAndFcmToken() {
        return queryFactory
                .select(Projections.fields(NotificatonMemberMemberIdAndTokenDto.class,
                        member.id.as("memberId"),
                        member.fcmToken.as("token")
                ))
                .from(member)
                .where(
                        member.delYn.eq(YN.N), member.statusCode.eq(MemberStatus.NORMAL),
                        member.fcmToken.isNotNull(), member.agreePushYn.eq(YN.Y)
                )
                .orderBy(member.id.desc())
                .fetch();
    }

    // 모든 마케팅 동의 멤버의 토큰과 id 가져오기
    public List<NotificatonMemberMemberIdAndTokenDto> findAllMarketingMemberIdAndFcmToken() {
        return queryFactory
                .select(Projections.fields(NotificatonMemberMemberIdAndTokenDto.class,
                        member.id.as("memberId"),
                        member.fcmToken.as("token")
                ))
                .from(member)
                .where(
                        member.delYn.eq(YN.N), member.statusCode.eq(MemberStatus.NORMAL),
                        member.fcmToken.isNotNull(), member.agreePushYn.eq(YN.Y),
                        member.agreeMarketingYn.eq(YN.Y)
                )
                .orderBy(member.id.desc())
                .fetch();
    }

    private Predicate condMemberIdsContain(List<Long> ids) {
        return ids == null || ids.isEmpty() ? null : member.id.in(ids);
    }

    private Predicate condKeyword(String s, String type) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!hasText(type) || !hasText(s)) return builder;

        switch (type) {
            case "all":
                builder.or(member.email.like("%" + s + "%"));
                builder.or(member.realName.like("%" + s + "%"));
                builder.or(member.mobileNumber.like("%" + s + "%"));
                break;

            case "email":
                builder.and(member.email.like("%" + s + "%"));
                break;

            case "name":
                builder.and(member.realName.like("%" + s + "%"));
                break;

            case "phone":
                builder.and(member.mobileNumber.like("%" + s + "%"));
                break;
        }

        return builder;
    }

    private Predicate condKeywordLike(String s) {
        return hasText(s) ? member.realName.contains(s).or(member.email.contains(s)).or(member.mobileNumber.contains(s)) : null;
    }

    private Predicate condStartDateAndEndDate(String startDate, String endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(startDate) && !hasText(endDate)) {
            builder.and(member.memberCreatedAt.goe(LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIN)));
        } else if (!hasText(startDate) && hasText(endDate)) {
            builder.and(member.memberCreatedAt.loe(LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX)));
        } else if (hasText(startDate) && hasText(endDate)) {
            builder.and(member.memberCreatedAt.between(LocalDate.parse(startDate).atTime(LocalTime.MIN), LocalDate.parse(endDate).atTime(LocalTime.MAX)));
        }
        return builder;
    }

    private Predicate condCreatedAtBetween(String range) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        LocalDateTime startOfWeek = today.minusDays(6).atStartOfDay();

        if ("today".equals(range)) {
            return new BooleanBuilder().and(member.memberCreatedAt.between(startOfDay, endOfDay));
        } else {
            return new BooleanBuilder().and(member.memberCreatedAt.between(startOfWeek, endOfDay));
        }
    }

    private OrderSpecifier<?>[] getSort(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;

            switch (order.getProperty()) {
                case "realName":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, member.realName));
                    break;

                case "withdrawAt":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, member.withdrawAt));
                    break;

                default:
                    orderSpecifiers.add(new OrderSpecifier<>(direction, member.id));
            }

        }

        return orderSpecifiers.toArray(OrderSpecifier[]::new);
    }

    private Predicate condIsHaveToken(YN isHaveToken) {
        return isHaveToken.equals(YN.Y) ? member.fcmToken.isNotNull() : null;
    }

    @Override
    public Page<MemberListDto> searchGetFcmTokenMember(MemberSearchDto condition, PageRequest request) {
        List<MemberListDto> content;

        content = queryFactory
                .select(Projections.fields(MemberListDto.class,
                        member.id,
                        member.email,
                        member.realName,
                        member.mobileNumber,
                        member.statusCode,
                        member.regCode,
                        member.lastLoginAt,
                        member.memberCreatedAt,
                        Expressions.as(JPAExpressions.select(Wildcard.count)
                                .from(purchaseInquiry)
                                .where(purchaseInquiry.memberId.eq(member.id).and(purchaseInquiry.delYn.eq(YN.N))), "purchaseCount"),
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(saleInquiry)
                                .where(saleInquiry.memberId.eq(member.id).and(saleInquiry.delYn.eq(YN.N))), "saleCount")
                ))
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()), member.fcmToken.isNotNull(),
                        member.statusCode.ne(MemberStatus.SECESSION)
                )
                .orderBy(getSort(request))
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .fetch();
        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()),
                        member.statusCode.ne(MemberStatus.SECESSION)
                );

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }

    @Override
    public Page<MemberListDto> searchGetFcmTokenMarketingMember(MemberSearchDto condition, PageRequest request) {
        List<MemberListDto> content;

        content = queryFactory
                .select(Projections.fields(MemberListDto.class,
                        member.id,
                        member.email,
                        member.realName,
                        member.mobileNumber,
                        member.statusCode,
                        member.regCode,
                        member.lastLoginAt,
                        member.memberCreatedAt,
                        Expressions.as(JPAExpressions.select(Wildcard.count)
                                .from(purchaseInquiry)
                                .where(purchaseInquiry.memberId.eq(member.id).and(purchaseInquiry.delYn.eq(YN.N))), "purchaseCount"),
                        Expressions.as(JPAExpressions.select(Wildcard.count).from(saleInquiry)
                                .where(saleInquiry.memberId.eq(member.id).and(saleInquiry.delYn.eq(YN.N))), "saleCount")
                ))
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()), member.fcmToken.isNotNull(),
                        member.statusCode.ne(MemberStatus.SECESSION),member.agreeMarketingYn.eq(YN.Y)
                )
                .orderBy(getSort(request))
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .fetch();
        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(member)
                .where(
                        condKeyword(condition.getS(), condition.getType()),
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condKeywordLike(condition.getS()),
                        member.statusCode.ne(MemberStatus.SECESSION)
                );

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }
}
