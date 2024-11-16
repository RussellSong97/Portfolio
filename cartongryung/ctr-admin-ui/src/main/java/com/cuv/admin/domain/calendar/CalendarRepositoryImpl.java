package com.cuv.admin.domain.calendar;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.calendar.dto.StatSearchDto;
import com.cuv.admin.domain.calendar.dto.consult.ConsultDto;
import com.cuv.admin.domain.calendar.dto.counslorDealer.CounselorDealerDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatMonthAndYearDto;
import com.cuv.admin.domain.calendar.entity.Calendar;
import com.cuv.admin.domain.calendar.entity.QCalendar;
import com.cuv.admin.domain.inquirydetail.entity.QInquiryDetail;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.member.entity.QMember;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.purchaseinquiry.entity.QPurchaseInquiry;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.saleinquiry.entity.QSaleInquiry;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.cuv.admin.domain.calendar.entity.QCalendar.calendar;
import static com.cuv.admin.domain.inquirydetail.entity.QInquiryDetail.inquiryDetail;
import static com.cuv.admin.domain.member.entity.QMember.member;
import static com.cuv.admin.domain.memberadmin.entity.QMemberAdmin.memberAdmin;
import static com.cuv.admin.domain.purchaseinquiry.entity.QPurchaseInquiry.purchaseInquiry;
import static com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus.*;

public class CalendarRepositoryImpl implements CalendarRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CalendarRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    /**
     * 날짜에 따른 회원 숫자. 회원 x인 날은 0으로 출력
     *
     * @param condition: 검색조건(startDate, endDate)
     */
    @Override
    public List<MemberStatDto> searchMemberStat(StatSearchDto condition) {
        // 날짜를 문자열로 포맷팅
        StringTemplate formattedMemberDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", member.memberCreatedAt);  // 여기서 에러 가능성 따로 임포트 안하고 요소 전부 다 임포트해서
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", calendar.calendarDate);

        // 회원 수를 계산하고 0일 경우 0으로 설정
        NumberExpression<Long> memberCount = new CaseBuilder()
                .when(member.id.count().eq(0L))
                .then(0L)
                .otherwise(member.id.count());

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusMonths(1).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusMonths(1).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }


        // 날짜 범위에 따른 조건 설정
        BooleanExpression dateRangeCondition = calendar.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        return queryFactory
                .select(Projections.fields(MemberStatDto.class,
                        calendar.calendarDate.as("date"),
                        memberCount.as("count")
                ))
                .from(calendar)
                .leftJoin(member).on(formattedCalendarDate.eq(formattedMemberDate)
                        .and(member.delYn.eq(YN.N))) // 삭제되지 않은 회원만 포함
                .where(dateRangeCondition)
                .groupBy(calendar.calendarDate)
                .orderBy(calendar.calendarDate.desc())
                .fetch();
    }


    /**
     * 월별
     * 월별 날짜에 따른 회원 숫자. 회원 x인 날은 0으로 출력
     *
     * @param condition: 검색조건(startDate, endDate)
     */
    @Override
    public List<MemberStatMonthAndYearDto> searchMemberStatMonth(StatSearchDto condition) {
        // 날짜를 문자열로 포맷팅
        StringTemplate formattedMemberDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", member.memberCreatedAt);

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위에 따른 조건 설정
        BooleanExpression dateRangeCondition = (member.memberCreatedAt.between(LocalDate.parse(startDate).atStartOfDay(), LocalDate.parse(endDate).atStartOfDay()));

        QMember subMember = new QMember(member);

        return queryFactory
                .select(Projections.fields(MemberStatMonthAndYearDto.class,
                        formattedMemberDate.as("date"),
                        member.id.count().as("count")
                ))
                .from(member)
                .where(member.id.in(
                        JPAExpressions
                                .select(subMember.id)
                                .from(subMember)
                                .groupBy(subMember.id)
                ))
                .where(dateRangeCondition)
                .groupBy(formattedMemberDate)
                .fetch();
    }

    /**
     * 년별
     * 년별 날짜에 따른 회원 숫자. 회원 x인 날은 0으로 출력
     *
     * @param condition: 검색조건(startDate, endDate)
     */
    @Override
    public List<MemberStatMonthAndYearDto> searchMemberStatYear(StatSearchDto condition) {
        // 날짜를 문자열로 포맷팅
        StringTemplate formattedMemberDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y')", member.memberCreatedAt);

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().withMonth(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위에 따른 조건 설정
        BooleanExpression dateRangeCondition = (member.memberCreatedAt.between(LocalDate.parse(startDate).atStartOfDay(), LocalDate.parse(endDate).atStartOfDay()));

        QMember subMember = new QMember(member);

        return queryFactory
                .select(Projections.fields(MemberStatMonthAndYearDto.class,
                        formattedMemberDate.as("date"),
                        member.id.count().as("count")
                ))
                .from(member)
                .where(member.id.in(
                        JPAExpressions
                                .select(subMember.id)
                                .from(subMember)
                                .groupBy(subMember.id)
                ))
                .where(dateRangeCondition)
                .groupBy(formattedMemberDate)
                .fetch();
    }

    /**
     * 판매
     *
     * @param condition: 검색조건
     */
    @Override
    public List<CounselorDealerDto> searchCounselorForSale(StatSearchDto condition) {
        StringTemplate formattedInquiryDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", inquiryDetail.createdAt);
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", calendar.calendarDate);
        /**
         * 기간 설정
         */

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위 조건 설정
        BooleanExpression dateRangeCondition = calendar.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        // 상담 완료 및 거래 완료 건수 계산
        NumberExpression<Long> completedCounsel = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(CONSULTATION_COMPLETE)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> completedTrade = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(PAYMENT_COMPLETE)).then(1L)
                .otherwise(0L).sum().coalesce(0L);

        // 쿼리 실행
        List<CounselorDealerDto> content = queryFactory
                .select(Projections.fields(CounselorDealerDto.class,
                        memberAdmin.id.coalesce(0L).as("id"),
                        memberAdmin.realName.coalesce("").as("realName"),
                        calendar.calendarDate.as("date"),
                        completedCounsel.as("completedCounsel"),
                        completedTrade.as("completedTrade")
                ))
                .from(calendar)
                .leftJoin(inquiryDetail).on(formattedCalendarDate.eq(formattedInquiryDate)
                        .and(inquiryDetail.delYn.eq(YN.N))
                        .and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL)))
                .leftJoin(memberAdmin).on(memberAdmin.id.eq(inquiryDetail.memberAdminId)
                        .and(memberAdmin.role.eq(MemberRole.COUNSELOR)))
                .where(dateRangeCondition,
                        // 상담원 선택
                        condCounselorAndDealerEq(condition.getCounselor()),
                        // 상태 선택
                        condConsultationStatusEq(condition.getStatus())
                )
                .groupBy(calendar.calendarDate, memberAdmin.id, memberAdmin.realName)
                .orderBy(calendar.calendarDate.asc())
                .fetch();

        return content;
    }

    /**
     * 상담원 구매
     *
     * @param condition: 검색조건
     */

    @Override
    public List<CounselorDealerDto> searchCounselorForPurchase(StatSearchDto condition) {
        StringTemplate formattedInquiryDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", inquiryDetail.createdAt);
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", calendar.calendarDate);
        /**
         * 기간 설정
         */

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위 조건 설정
        BooleanExpression dateRangeCondition = calendar.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        // 상담 완료 및 거래 완료 건수 계산
        NumberExpression<Long> completedCounsel = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(CONSULTATION_COMPLETE)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> completedTrade = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(PAYMENT_COMPLETE)).then(1L)
                .otherwise(0L).sum().coalesce(0L);

        // 쿼리 실행
        List<CounselorDealerDto> content = queryFactory
                .select(Projections.fields(CounselorDealerDto.class,
                        memberAdmin.id.coalesce(0L).as("id"),
                        memberAdmin.realName.coalesce("").as("realName"),
                        calendar.calendarDate.as("date"),
                        completedCounsel.as("completedCounsel"),
                        completedTrade.as("completedTrade")
                ))
                .from(calendar)
                .leftJoin(inquiryDetail).on(formattedCalendarDate.eq(formattedInquiryDate)
                        .and(inquiryDetail.delYn.eq(YN.N))
                        .and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY)))
                .leftJoin(memberAdmin).on(memberAdmin.id.eq(inquiryDetail.memberAdminId)
                        .and(memberAdmin.role.eq(MemberRole.COUNSELOR)))
                .where(dateRangeCondition,
                        // 상담원 선택
                        condCounselorAndDealerEq(condition.getCounselor()),
                        // 상태 선택
                        condConsultationStatusEq(condition.getStatus())
                )
                .groupBy(calendar.calendarDate, memberAdmin.id, memberAdmin.realName)
                .orderBy(calendar.calendarDate.asc())
                .fetch();

        return content;
    }


    /**
     * 딜러 구매
     *
     * @param condition: 검색조건
     */

    @Override
    public List<CounselorDealerDto> searchDealerForPurchase(StatSearchDto condition) {
        StringTemplate formattedInquiryDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", inquiryDetail.createdAt);
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", calendar.calendarDate);
        /**
         * 기간 설정
         */

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위 조건 설정
        BooleanExpression dateRangeCondition = calendar.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        // 상담 완료 및 거래 완료 건수 계산
        NumberExpression<Long> completedCounsel = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(CONSULTATION_COMPLETE)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> completedTrade = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(PAYMENT_COMPLETE)).then(1L)
                .otherwise(0L).sum().coalesce(0L);

        // 쿼리 실행
        List<CounselorDealerDto> content = queryFactory
                .select(Projections.fields(CounselorDealerDto.class,
                        memberAdmin.id.coalesce(0L).as("id"),
                        memberAdmin.realName.coalesce("").as("realName"),
                        calendar.calendarDate.as("date"),
                        completedCounsel.as("completedCounsel"),
                        completedTrade.as("completedTrade")
                ))
                .from(calendar)
                .leftJoin(inquiryDetail).on(formattedCalendarDate.eq(formattedInquiryDate)
                        .and(inquiryDetail.delYn.eq(YN.N))
                        .and(inquiryDetail.tradeTypeCode.eq(TradeType.BUY)))
                .leftJoin(memberAdmin).on(memberAdmin.id.eq(inquiryDetail.memberAdminId)
                        .and(memberAdmin.role.eq(MemberRole.DEALER)))
                .where(dateRangeCondition,
                        // 상담원 선택
                        condCounselorAndDealerEq(condition.getDealer()),
                        // 상태 선택
                        condConsultationStatusEq(condition.getStatus())
                )
                .groupBy(calendar.calendarDate, memberAdmin.id, memberAdmin.realName)
                .orderBy(calendar.calendarDate.asc())
                .fetch();

        return content;
    }

    /**
     * 딜러 판매
     *
     * @param condition: 검색조건
     */

    @Override
    public List<CounselorDealerDto> searchDealerForSale(StatSearchDto condition) {
        StringTemplate formattedInquiryDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", inquiryDetail.createdAt);
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", calendar.calendarDate);
        /**
         * 기간 설정
         */

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위 조건 설정
        BooleanExpression dateRangeCondition = calendar.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        // 상담 완료 및 거래 완료 건수 계산
        NumberExpression<Long> completedCounsel = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(CONSULTATION_COMPLETE)).then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> completedTrade = new CaseBuilder()
                .when(inquiryDetail.consultationStatus.eq(PAYMENT_COMPLETE)).then(1L)
                .otherwise(0L).sum().coalesce(0L);

        // 쿼리 실행
        List<CounselorDealerDto> content = queryFactory
                .select(Projections.fields(CounselorDealerDto.class,
                        memberAdmin.id.coalesce(0L).as("id"),
                        memberAdmin.realName.coalesce("").as("realName"),
                        calendar.calendarDate.as("date"),
                        completedCounsel.as("completedCounsel"),
                        completedTrade.as("completedTrade")
                ))
                .from(calendar)
                .leftJoin(inquiryDetail).on(formattedCalendarDate.eq(formattedInquiryDate)
                        .and(inquiryDetail.delYn.eq(YN.N))
                        .and(inquiryDetail.tradeTypeCode.eq(TradeType.SELL)))
                .leftJoin(memberAdmin).on(memberAdmin.id.eq(inquiryDetail.memberAdminId)
                        .and(memberAdmin.role.eq(MemberRole.DEALER)))
                .where(dateRangeCondition,
                        // 상담원 선택
                        condCounselorAndDealerEq(condition.getDealer()),
                        // 상태 선택
                        condConsultationStatusEq(condition.getStatus())
                )
                .groupBy(calendar.calendarDate, memberAdmin.id, memberAdmin.realName)
                .orderBy(calendar.calendarDate.asc())
                .fetch();

        return content;
    }


    /**
     * 상담현황 구매
     *
     * @param condition: 검색조건
     */
    @Override
    public List<ConsultDto> searchConsultForPurchase(StatSearchDto condition) {
        QCalendar c = QCalendar.calendar;
        QPurchaseInquiry pi = QPurchaseInquiry.purchaseInquiry;
        QInquiryDetail id = QInquiryDetail.inquiryDetail;

        // Date formatting templates
        StringTemplate formattedInquiryDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", pi.createdAt);
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", c.calendarDate);

        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // Date range condition
        BooleanExpression dateRangeCondition = c.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        // Count expressions
        NumberExpression<Long> reservationCount = new CaseBuilder()
                .when(pi.visitReservationAt.isNotNull())
                .then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> rejectedCount = new CaseBuilder()
                .when(id.consultationStatus.eq(DISAPPROVED))
                .then(1L)
                .otherwise(0L).sum();

        // Main query
        List<ConsultDto> content = queryFactory
                .select(Projections.fields(
                        ConsultDto.class,
                        c.calendarDate.as("date"),
                        new CaseBuilder()
                                .when(pi.id.count().isNull())
                                .then(0L)
                                .otherwise(pi.id.count()).as("consultCountAll"),
                        new CaseBuilder()
                                .when(reservationCount.isNull())
                                .then(0L)
                                .otherwise(reservationCount).as("reservationConsultCount"),
                        new CaseBuilder()
                                .when(rejectedCount.isNull())
                                .then(0L)
                                .otherwise(rejectedCount).as("rejectedConsultCount")
                ))
                .from(c)
                .leftJoin(pi).on(formattedInquiryDate.eq(formattedCalendarDate).and(pi.delYn.eq(YN.N)))
                .leftJoin(id).on(pi.id.eq(id.inquiryId))
                .where(dateRangeCondition, condConsultStatusV1(condition.getStatus()))
                .groupBy(c.calendarDate)
                .orderBy(c.calendarDate.desc())
                .fetch();

        return content;
    }

    /**
     * 상담현황 판매
     *
     * @param condition: 검색조건
     */
    @Override
    public List<ConsultDto> searchConsultForSale(StatSearchDto condition) {
        QCalendar c = QCalendar.calendar;
        QSaleInquiry si = QSaleInquiry.saleInquiry;
        QInquiryDetail id = QInquiryDetail.inquiryDetail;

        // Date formatting templates
        StringTemplate formattedInquiryDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", si.createdAt);
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", c.calendarDate);

        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // Date range condition
        BooleanExpression dateRangeCondition = c.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        // Count expressions
        NumberExpression<Long> reservationCount = new CaseBuilder()
                .when(si.visitReservationAt.isNotNull())
                .then(1L)
                .otherwise(0L).sum();

        NumberExpression<Long> rejectedCount = new CaseBuilder()
                .when(id.consultationStatus.eq(DISAPPROVED))
                .then(1L)
                .otherwise(0L).sum();

        // Main query
        List<ConsultDto> content = queryFactory
                .select(Projections.fields(
                        ConsultDto.class,
                        c.calendarDate.as("date"),
                        new CaseBuilder()
                                .when(si.id.count().isNull())
                                .then(0L)
                                .otherwise(si.id.count()).as("consultCountAll"),
                        new CaseBuilder()
                                .when(reservationCount.isNull())
                                .then(0L)
                                .otherwise(reservationCount).as("reservationConsultCount"),
                        new CaseBuilder()
                                .when(rejectedCount.isNull())
                                .then(0L)
                                .otherwise(rejectedCount).as("rejectedConsultCount")
                ))
                .from(c)
                .leftJoin(si).on(formattedInquiryDate.eq(formattedCalendarDate).and(si.delYn.eq(YN.N)))
                .leftJoin(id).on(si.id.eq(id.inquiryId))
                .where(dateRangeCondition, condConsultStatusV1(condition.getStatus()))
                .groupBy(c.calendarDate)
                .orderBy(c.calendarDate.desc())
                .fetch();

        return content;
    }

    @Override
    public List<MemberStatDto> searchMemberStatExcel(StatSearchDto condition) {
        // 날짜를 문자열로 포맷팅
        StringTemplate formattedMemberDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", member.memberCreatedAt);  // 여기서 에러 가능성 따로 임포트 안하고 요소 전부 다 임포트해서
        StringTemplate formattedCalendarDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", calendar.calendarDate);

        // 회원 수를 계산하고 0일 경우 0으로 설정
        NumberExpression<Long> memberCount = new CaseBuilder()
                .when(member.id.count().eq(0L))
                .then(0L)
                .otherwise(member.id.count());

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusMonths(1).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusMonths(1).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위에 따른 조건 설정
        BooleanExpression dateRangeCondition = calendar.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        return queryFactory
                .select(Projections.fields(MemberStatDto.class,
                        calendar.calendarDate.as("date"),
                        memberCount.as("count")
                ))
                .from(calendar)
                .leftJoin(member).on(formattedCalendarDate.eq(formattedMemberDate)
                        .and(member.delYn.eq(YN.N))) // 삭제되지 않은 회원만 포함
                .where(dateRangeCondition)
                .groupBy(calendar.calendarDate)
                .orderBy(calendar.calendarDate.desc())
                .fetch();
    }

    @Override
    public List<MemberStatMonthAndYearDto> searchMemberStatMonthExcel(StatSearchDto condition) {
        // 날짜를 문자열로 포맷팅
        StringTemplate formattedMemberDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", member.memberCreatedAt);

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위에 따른 조건 설정
        BooleanExpression dateRangeCondition = (member.memberCreatedAt.between(LocalDate.parse(startDate).atStartOfDay(), LocalDate.parse(endDate).atStartOfDay()));

        QMember subMember = new QMember(member);

        return queryFactory
                .select(Projections.fields(MemberStatMonthAndYearDto.class,
                        formattedMemberDate.as("date"),
                        member.id.count().as("count")
                ))
                .from(member)
                .where(member.id.in(
                        JPAExpressions
                                .select(subMember.id)
                                .from(subMember)
                                .groupBy(subMember.id)
                ))
                .where(dateRangeCondition)
                .groupBy(formattedMemberDate)
                .fetch();
    }

    @Override
    public List<MemberStatMonthAndYearDto> searchMemberStatYearExcel(StatSearchDto condition) {
        // 날짜를 문자열로 포맷팅
        StringTemplate formattedMemberDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y')", member.memberCreatedAt);

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusMonths(1).withDayOfMonth(1).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위에 따른 조건 설정
        BooleanExpression dateRangeCondition = (member.memberCreatedAt.between(LocalDate.parse(startDate).atStartOfDay(), LocalDate.parse(endDate).atStartOfDay()));

        QMember subMember = new QMember(member);

        return queryFactory
                .select(Projections.fields(MemberStatMonthAndYearDto.class,
                        formattedMemberDate.as("date"),
                        member.id.count().as("count")
                ))
                .from(member)
                .where(member.id.in(
                        JPAExpressions
                                .select(subMember.id)
                                .from(subMember)
                                .groupBy(subMember.id)
                ))
                .where(dateRangeCondition)
                .groupBy(formattedMemberDate)
                .fetch();
    }

    /**
     * 켈린더 불러오기
     *
     * @param condition: 기간 설정
     */

    @Override
    public List<Calendar> searchCalendar(StatSearchDto condition) {
        /**
         * 기간 설정
         */

        // 날짜 변수
        String endDate = null;
        String startDate = null;

        // 기간 설정 x
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            condition.setEndDate(LocalDate.now().toString());
            endDate = condition.getEndDate();
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();

            // 시작일만 설정됨
        } else if (condition.getEndDate().isEmpty() && condition.getStartDate() != null) {
            startDate = condition.getStartDate();
            condition.setEndDate(LocalDate.now().toString());
            endDate = LocalDate.now().toString();

            // 끝나는 일만 설정됨
        } else if (Objects.requireNonNull(condition.getStartDate()).isEmpty() && condition.getEndDate() != null) {
            condition.setStartDate(LocalDate.now().minusDays(2).toString());
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 다 설정
        else if (condition.getStartDate() != null && condition.getEndDate() != null) {
            startDate = condition.getStartDate();
            endDate = condition.getEndDate();
        }

        // 날짜 범위 조건 설정
        BooleanExpression dateRangeCondition = calendar.calendarDate.between(LocalDate.parse(startDate), LocalDate.parse(endDate));

        // 쿼리 실행
        List<Calendar> content = queryFactory
                .select(Projections.fields(Calendar.class,
                        calendar.id.as("calendarId"),
                        calendar.calendarDate
                ))
                .from(calendar)
                .where(dateRangeCondition
                )
                .orderBy(calendar.calendarDate.desc())
                .fetch();

        return content;
    }

    // 상담사, 딜러 삼담 상태 선택
    private Predicate condConsultationStatusEq(List<String> strConsultationStatus) {
        BooleanBuilder builder = new BooleanBuilder();

        if (strConsultationStatus == null || strConsultationStatus.isEmpty()) {
            return builder;
        }

        for (String status : strConsultationStatus) {
            ConsultationStatus saleConsultationStatus = ConsultationStatus.ofCode(status);
            builder.or(inquiryDetail.consultationStatus.eq(saleConsultationStatus));
        }

        return builder;
    }

    private Predicate condCounselorAndDealerEq(List<Long> idList) {
        BooleanBuilder builder = new BooleanBuilder();

        if (idList == null || String.valueOf(idList).isEmpty()) {
            return builder;
        }

        for (Long id : idList) {
            builder.or(memberAdmin.id.eq(id));
        }
        return builder;
    }

    // 상담 현황 상태 적용하기 1
    private Predicate condConsultStatusV1(List<String> consultationStatus) {

        QPurchaseInquiry pi = purchaseInquiry;

        BooleanBuilder builder = new BooleanBuilder();
        if (consultationStatus == null || consultationStatus.isEmpty()) {
            return builder;
        }

        for (String status : consultationStatus) {
            switch (status) {
                case "consultAll":
                    builder.and(pi.delYn.eq(YN.N));
                    break;
                case "reservation":
                    builder.and(pi.visitReservationAt.isNotNull());
                    break;
                case "reject":
                    builder.and(inquiryDetail.consultationStatus.eq(ConsultationStatus.DISAPPROVED));
                    break;
                default:
                    break;
            }
        }

        return builder;
    }

}
