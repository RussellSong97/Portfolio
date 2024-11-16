package com.cuv.domain.notification;

import com.cuv.common.YN;
import com.cuv.domain.notification.dto.NotificationListDto;
import com.cuv.domain.pick.dto.PickListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cuv.domain.notification.entity.QNotification.notification;
import static com.cuv.domain.notificationMember.entity.QNotificationMember.notificationMember;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NotificationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * @param memberId: 맴버 ID
     *                  멤버 아이디로 알림 가져오기
     * @author 박성민
     */
    @Override
    public List<NotificationListDto> searchNotificationListNoPageList(Long memberId) {
        List<NotificationListDto> content;
        content = queryFactory
                .select(Projections.fields(NotificationListDto.class,
                        notification.id.as("notificationId"),
                        notificationMember.memberId,
                        notification.linkUrl,
                        notification.content,
                        notification.readYn,
                        notification.createdAt.as("createdAt")))
                .from(notificationMember)
                .leftJoin(notification).on(notification.id.eq(notificationMember.notificationId))
                .where(notificationMember.memberId.eq(memberId), notificationMember.delYn.eq(YN.N),
                        notification.delYn.eq(YN.N)
                        )
                .orderBy(notification.createdAt.desc())  // 최근 시간순으로 정렬
                .fetch();
        return content;
    }

    @Override
    public Long searchNotificationsMemberIdReadYn(Long id) {
        return queryFactory
                .select(notificationMember.notificationId)
                .from(notificationMember)
                .leftJoin(notification).on(notification.id.eq(notificationMember.notificationId))
                .where(notificationMember.memberId.eq(id)
                        .and(notificationMember.readYn.eq(YN.N))
                        .and(notificationMember.delYn.eq(YN.N)))
                .fetchFirst();
    }
}
