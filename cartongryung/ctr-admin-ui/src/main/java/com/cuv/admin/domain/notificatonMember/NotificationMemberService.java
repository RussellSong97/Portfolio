package com.cuv.admin.domain.notificatonMember;

import com.cuv.admin.domain.member.MemberRepository;
import com.cuv.admin.domain.member.dto.MemberListDto;
import com.cuv.admin.domain.member.dto.MemberSearchDto;
import com.cuv.admin.domain.notification.NotificationRepository;
import com.cuv.admin.domain.notificatonMember.dto.NotificationMemberDetailDto;
import com.cuv.admin.domain.notificatonMember.dto.NotificationMemberSaveDto;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 알림 맴버 (저장, 조회, 삭제)
 */
@Service
@RequiredArgsConstructor
public class NotificationMemberService {

    private final NotificationMemberRepository notificationMemberRepository;
    private final MemberRepository memberRepository;


    /**
     * 관리자 | 회원 > 푸시알림 > 푸시알림 보낸 맴버 저장하기
     * @param notificationMemberSaveDto 저장 정보
     */
    public void insertNotificationMember(NotificationMember notificationMemberSaveDto) {
        notificationMemberRepository.save(notificationMemberSaveDto);
    }

    /**
     * 관리자 | 회원 > 푸시알림 > 푸시알림 보낸 맴버 가져오기
     * @param notificationId 알림 아이디
     */

    public List<NotificationMemberDetailDto> findRealNameById(Long notificationId) {



        List<NotificationMemberDetailDto>  notificationMemberDetailDtoList = notificationMemberRepository.searchNotificatioIdsMemberId(notificationId);


        return notificationMemberDetailDtoList;
    }

    /**
     * 관리자 | 회원 -> 알림 업데이트 위해 맴버 제거
     * 관리자 | 회원 > 푸시알림 > 푸시알림 보낸 맴버 제거
     * @param notificationId 알림 아이디
     */
    public void deleteNotificationMemberMemberId(Long notificationId) {

        notificationMemberRepository.searchDeleteNotificationMemberMemberId(notificationId);
    }

}
