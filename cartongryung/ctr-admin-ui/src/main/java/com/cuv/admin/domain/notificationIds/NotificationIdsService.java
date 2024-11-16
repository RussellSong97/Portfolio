package com.cuv.admin.domain.notificationIds;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 알림 관련 아이디들 조회
 */
@Service
@RequiredArgsConstructor
public class NotificationIdsService {

    private final NotificationIdsRepository notificationIdsRepository;

    /**
     * 관리자 | 회원 > 푸시알림 > 예약 발송할 푸시알림 db에서 가져오기
     * @param notificationId : 알림 id
     *
     */
    public List<Long> getScheduledNotification(Long notificationId){
        return notificationIdsRepository.getScheduledMemberId(notificationId);
    }
}
