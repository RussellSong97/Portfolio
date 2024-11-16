package com.cuv.admin.domain.notificationIds;

import com.cuv.admin.domain.notification.entity.Notification;
import com.cuv.admin.domain.notificationIds.entity.NotificationIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NotificationIdsRepository extends
        JpaRepository<NotificationIds, Long>,
        QuerydslPredicateExecutor<NotificationIds>,
        NotificationIdsRepositoryCustom {

}
