package com.cuv.domain.notificationMember;

import com.cuv.common.YN;
import com.cuv.domain.notificationMember.entity.NotificationMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.cuv.domain.notificationMember.entity.QNotificationMember.notificationMember;

public class NotificationMemberRepositoryImpl implements NotificationMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public NotificationMemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    /**
     * 알림 제거 및 읽기로 만들기 위해 호출
     * @param notificationId
     * @param memberId
     * @return
     */
    @Override
    public NotificationMember searchNotificationIdAndMemberId(Long notificationId, Long memberId) {
        return queryFactory
                .selectFrom(notificationMember)
                .where(
                        notificationMember.notificationId.eq(notificationId),
                        notificationMember.memberId.eq(memberId),
                        notificationMember.delYn.eq(YN.N)
                )
                .fetchOne();

    }

    @Override
    public List<NotificationMember> searchNotificationMemberByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(notificationMember)
                .where(
                        notificationMember.memberId.eq(memberId),
                        notificationMember.delYn.eq(YN.N)
                )
                .fetch();
    }
}
