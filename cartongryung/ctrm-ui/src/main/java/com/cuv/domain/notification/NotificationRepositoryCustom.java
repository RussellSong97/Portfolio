package com.cuv.domain.notification;

import com.cuv.domain.notification.dto.NotificationListDto;
import com.cuv.domain.notificationMember.entity.NotificationMember;

import java.util.List;

public interface NotificationRepositoryCustom {

    List<NotificationListDto> searchNotificationListNoPageList(Long memberId);

    Long searchNotificationsMemberIdReadYn(Long id);

}
