package com.cuv.admin.domain.notification;


import com.cuv.admin.domain.notification.dto.NotificationDetail;
import com.cuv.admin.domain.notification.dto.NotificationListDto;
import com.cuv.admin.domain.notification.dto.NotificationSaveDto;
import com.cuv.admin.domain.notification.dto.NotificationSearchDto;
import com.cuv.admin.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface NotificationRepositoryCustom {

    Page<NotificationListDto> searchAllNotificationForListing(NotificationSearchDto condition, PageRequest request);

    NotificationDetail searchNotificationById(Long id);

    List<NotificationSaveDto> getScheduledNotification();

}
