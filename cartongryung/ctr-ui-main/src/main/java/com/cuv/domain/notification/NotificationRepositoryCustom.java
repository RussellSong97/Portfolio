package com.cuv.domain.notification;

import com.cuv.domain.notification.dto.NotificationListDto;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<NotificationListDto> searchNotificationListNoPageList(Long memberId);

    Long searchNotificationsMemberIdReadYn(Long id);
}
