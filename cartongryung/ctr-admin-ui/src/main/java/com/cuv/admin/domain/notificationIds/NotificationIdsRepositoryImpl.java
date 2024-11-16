package com.cuv.admin.domain.notificationIds;

import com.cuv.admin.domain.notification.dto.NotificationSaveDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cuv.admin.domain.notification.entity.QNotification.notification;
import static com.cuv.admin.domain.notificationIds.entity.QNotificationIds.notificationIds;

public class NotificationIdsRepositoryImpl implements NotificationIdsRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NotificationIdsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Long> getScheduledMemberId(Long notificationId) {
        return queryFactory
                .select(
                        notificationIds.memberId

                )
                .from(notificationIds)
                .where(
                        notificationIds.notificationId.eq(notificationId)
                )
                .fetch();
    }

    @Override
    public List<Long> getNotificationIds(Long notificationId) {
        return queryFactory
        .select(
                notificationIds.id

        )
                .from(notificationIds)
                .where(
                        notificationIds.notificationId.eq(notificationId)
                )
                .fetch();
    }


}
