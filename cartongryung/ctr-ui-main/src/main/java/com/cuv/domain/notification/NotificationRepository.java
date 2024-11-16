package com.cuv.domain.notification;

import com.cuv.domain.notification.entity.Notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends
        JpaRepository<Notification, Long>,
        NotificationRepositoryCustom,
        QuerydslPredicateExecutor<Notification> {

    Notification findNotificationById(Long notificatonId);



}
