package com.cuv.admin.domain.notification;

import com.cuv.admin.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NotificationRepository  extends
        JpaRepository<Notification, Long>,
        QuerydslPredicateExecutor<Notification>,
        NotificationRepositoryCustom {
}
