package com.cuv.admin.domain.notification;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.admin.domain.notification.dto.NotificationDetail;
import com.cuv.admin.domain.notification.dto.NotificationListDto;
import com.cuv.admin.domain.notification.dto.NotificationSaveDto;
import com.cuv.admin.domain.notification.dto.NotificationSearchDto;
import com.cuv.admin.domain.notification.entity.Notification;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.cuv.admin.domain.boardreview.entity.QBoardReview.boardReview;
import static com.cuv.admin.domain.notification.entity.QNotification.notification;
import static com.cuv.admin.domain.product.entity.QProduct.product;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class NotificationRepositoryImpl implements NotificationRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NotificationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<NotificationListDto> searchAllNotificationForListing(NotificationSearchDto condition, PageRequest request) {
        List<NotificationListDto> content;

        content = queryFactory
                .select(Projections.fields(NotificationListDto.class,
                        notification.id.as("notificationId"),
                        notification.sendStatus,
                        notification.createdAt,
                        notification.sendAt,
                        notification.content
                ))
                .from(notification)
                .where(
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condContentLike(condition.getS()),
                        condDelYnEqN()
                )
                .orderBy(notification.createdAt.desc())
                .offset(request.getOffset())
                .limit(request.getPageSize())
                .fetch();

        JPQLQuery<Long> contentQuery = queryFactory
                .select(Wildcard.count)
                .from(notification)
                .where(
                        condStartDateAndEndDate(condition.getStartDate(), condition.getEndDate()),
                        condContentLike(condition.getS()),
                        condDelYnEqN()
                );

        return PageableExecutionUtils.getPage(content, request, contentQuery::fetchCount);
    }

    @Override
    public NotificationDetail searchNotificationById(Long id) {
        return queryFactory.select(Projections.fields(NotificationDetail.class,
                        notification.id.as("notificationId"),
                        notification.target,
                        notification.sendStatus,
                        notification.title,
                        notification.content,
                        notification.attachment,
                        notification.linkUrl,
                        notification.sendAt
                ))
                .from(notification)
                .where(notification.id.eq(id), notification.delYn.eq(YN.N))
                .fetchOne();
    }

    @Override
    public List<NotificationSaveDto> getScheduledNotification() {
        // 수정하기
        LocalDateTime now = LocalDateTime.now();
        return queryFactory
                .select(Projections.fields(NotificationSaveDto.class,
                        notification.id.as("notificationId"),
                        notification.target,
                        notification.sendStatus,
                        notification.linkUrl,
                        notification.content,
                        notification.attachment,
                        notification.title,
                        notification.sendAt

                ))
                .from(notification)
                .where(
                        notification.sendStatus.eq("reserve"),
                        condDelYnEqN(), notification.sendAt.loe(now),
                        notification.pushStatus.eq("stay")
                )
                .orderBy(notification.createdAt.desc())
                .fetch();
    }


    private Predicate condDelYnEqN() {
        return new BooleanBuilder().and(notification.delYn.eq(YN.N));
    }

    private Predicate condContentLike(String s) {
        return hasText(s) ? notification.content.contains(s) : null;
    }

    private Predicate condStartDateAndEndDate(String startDate, String endDate) {
        BooleanBuilder builder = new BooleanBuilder();
        if (hasText(startDate) && !hasText(endDate)) {
            builder.and(notification.createdAt.goe(LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIN)));
        } else if (!hasText(startDate) && hasText(endDate)) {
            builder.and(notification.createdAt.loe(LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX)));
        } else if (hasText(startDate) && hasText(endDate)) {
            builder.and(notification.createdAt.between(LocalDate.parse(startDate).atTime(LocalTime.MIN), LocalDate.parse(endDate).atTime(LocalTime.MAX)));
        }
        return builder;
    }


}
