package com.cuv.admin.domain.notificationIds;

import com.cuv.admin.domain.notification.entity.Notification;

import java.util.List;

public interface NotificationIdsRepositoryCustom {
    List<Long> getScheduledMemberId(Long notificationId);

    List<Long> getNotificationIds(Long notificationId);
}

