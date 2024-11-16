package com.cuv.admin.domain.notificatonMember;

import com.cuv.admin.domain.notificatonMember.dto.NotificationMemberDetailDto;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;

import java.util.List;

public interface NotificationMemberRepositoryCustom {

    List<Long> searchNotificationMemberMemberId(Long notificationId);

    void searchDeleteNotificationMemberMemberId(Long notificationId);

    List<NotificationMemberDetailDto> searchNotificatioIdsMemberId(Long notificationId);

}
