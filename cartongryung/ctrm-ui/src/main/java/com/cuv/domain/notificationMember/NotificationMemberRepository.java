package com.cuv.domain.notificationMember;

import com.cuv.common.YN;
import com.cuv.domain.notification.NotificationRepositoryCustom;
import com.cuv.domain.notification.entity.Notification;
import com.cuv.domain.notificationMember.entity.NotificationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NotificationMemberRepository extends
        JpaRepository<NotificationMember, Long>,
        NotificationMemberRepositoryCustom,
        QuerydslPredicateExecutor<NotificationMember> {

    @Modifying
    @Transactional
    @Query("UPDATE NotificationMember nm SET nm.delYn = :delYn WHERE nm.notificationId = :notificationId AND nm.memberId = :memberId")
    void updateDelYnByNotificationIdAndMemberId(@Param("notificationId") Long notificationId, @Param("memberId") Long memberId, @Param("delYn") YN delYn);



}
