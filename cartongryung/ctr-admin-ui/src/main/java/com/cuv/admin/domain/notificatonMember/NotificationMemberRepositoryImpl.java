package com.cuv.admin.domain.notificatonMember;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.notification.dto.NotificationDetail;
import com.cuv.admin.domain.notificatonMember.dto.NotificationMemberDetailDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cuv.admin.domain.member.entity.QMember.member;
import static com.cuv.admin.domain.notification.entity.QNotification.notification;
import static com.cuv.admin.domain.notificationIds.entity.QNotificationIds.notificationIds;
import static com.cuv.admin.domain.notificatonMember.entity.QNotificationMember.notificationMember;

public class NotificationMemberRepositoryImpl implements NotificationMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NotificationMemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Long> searchNotificationMemberMemberId(Long notificationId) {
        return queryFactory
                .select(notificationMember.memberId)
                .from(notificationMember)
                .where(notificationMember.notificationId.eq(notificationId))
                .fetch();
    }

    @Override
    public void searchDeleteNotificationMemberMemberId(Long notificationId) {
        queryFactory
                .update(notificationMember)
                .set(notificationMember.memberId, (Long) null)
                .where(notificationMember.notificationId.eq(notificationId))
                .execute();
    }

    @Override
    public List<NotificationMemberDetailDto> searchNotificatioIdsMemberId(Long notificationId) {
        return queryFactory
                .select(Projections.fields(NotificationMemberDetailDto.class,
                        notificationIds.memberId,
                        member.realName
                ))
                .from(notificationIds)
                .leftJoin(member).on(notificationIds.memberId.eq(member.id))
                .where(notificationIds.notificationId.eq(notificationId))
                .fetch();
    }
}
