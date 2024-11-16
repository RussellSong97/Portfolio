package com.cuv.domain.notification;

import com.cuv.common.YN;
import com.cuv.domain.notification.dto.NotificationListDto;
import com.cuv.domain.pick.dto.PickListDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cuv.domain.notification.entity.QNotification.notification;

public class NotificationRepositoryImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NotificationRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * @param memberId: 맴버 ID
     *  멤버 아이디로 알림 가져오기
     * @author 박성민
     */
    @Override
    public List<NotificationListDto> searchNotificationListNoPageList(Long memberId) {
        List<NotificationListDto> content;
        content = queryFactory
                .select(Projections.fields(NotificationListDto.class,
                        notification.id.as("notificationId"),
                        notification.memberId,
                        notification.linkUrl,
                        notification.content,
                        notification.attachment,
                        notification.readYn,
                        notification.createdAt.as("createdAt")))
                .from(notification)
                .where(notification.memberId.eq(memberId))
                .where(notification.delYn.eq(YN.N))
                        .fetch();
        return content;
    }

    @Override
    public Long searchNotificationsMemberIdReadYn(Long id) {
        return queryFactory
                .select(notification.id)
                .from(notification)
                .where(notification.memberId.eq(id)
                        .and(notification.readYn.eq(YN.N))
                        .and(notification.delYn.eq(YN.N)))
                .fetchFirst();
    }
}
