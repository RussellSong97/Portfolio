package com.cuv.domain.notificationMember;

import com.cuv.domain.notificationMember.entity.NotificationMember;

import java.util.List;

public interface NotificationMemberRepositoryCustom {

    NotificationMember searchNotificationIdAndMemberId(Long notificationId, Long memberId);

    List<NotificationMember> searchNotificationMemberByMemberId(Long memberId);
}
