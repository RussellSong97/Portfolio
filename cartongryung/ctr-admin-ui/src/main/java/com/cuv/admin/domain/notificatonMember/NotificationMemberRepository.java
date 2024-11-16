package com.cuv.admin.domain.notificatonMember;

import com.cuv.admin.domain.notification.NotificationRepositoryCustom;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NotificationMemberRepository extends
        JpaRepository<NotificationMember, Long>,
        QuerydslPredicateExecutor<NotificationMember>,
        NotificationMemberRepositoryCustom {
}
