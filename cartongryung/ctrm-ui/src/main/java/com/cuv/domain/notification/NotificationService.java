package com.cuv.domain.notification;

import com.cuv.common.YN;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.notification.dto.NotificationListDto;
import com.cuv.domain.notification.entity.Notification;
import com.cuv.domain.notificationMember.NotificationMemberRepository;
import com.cuv.domain.notificationMember.entity.NotificationMember;
import com.cuv.domain.pick.entity.Pick;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
public class NotificationService {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;

    /**
     * 알림 | memberId로 알림들 가져오기
     *
     * @author 박성민
     */
    public List<NotificationListDto> getNotificationsById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴
        if (member == null) {
            // member가 null일 경우 예외 처리
            throw new UsernameNotFoundException("Member not found with email: " + email);
        }
        Long memberId = member.getId();

        List<NotificationListDto> uniqueNotificationMemberList = new ArrayList<>();

        try {
            List<NotificationListDto> notificationList = notificationRepository.searchNotificationListNoPageList(memberId);
            // 같은 notificationId를 가진 NotificationMember를 하나만 남기기
            Map<Long, NotificationListDto> uniqueNotificationMap = new LinkedHashMap<>();

            for (NotificationListDto NotificationListDto : notificationList) {
                // notificationId를 키로 사용하여 Map에 추가 (중복된 키가 있으면 이전 항목을 덮어씌움)
                uniqueNotificationMap.put(NotificationListDto.getNotificationId(), NotificationListDto);
            }
            // Map의 값들만으로 리스트 생성 (중복이 제거된 NotificationMember 리스트)
             uniqueNotificationMemberList = new ArrayList<>(uniqueNotificationMap.values());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return uniqueNotificationMemberList;
    }

    /**
     * 알림 | getNotificationsById메소드로 가져온 notification들의 readYn 변경
     *
     * @author 박성민
     */
    public void updateReadYn() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴
        if (member == null) {
            // member가 null일 경우 예외 처리
            throw new UsernameNotFoundException("Member not found with email: " + email);
        }
        Long memberId = member.getId();

        try {
            List<NotificationMember> notificationMemberList = notificationMemberRepository.searchNotificationMemberByMemberId(memberId);
            // 같은 알림 아이디 있는 것은 제거해서 1개만 남기기
            notificationMemberList.forEach(notificationMember -> notificationMember.setReadYn(YN.Y));
            notificationMemberRepository.saveAll(notificationMemberList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 알림 | 알림맴버 디비에 저장한 거 notificationId들로 제거
     *
     * @author 박성민
     */
    public void updateDelYn(Long notificationId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL); // 이메일로 맴버 객체 가져옴
        if (member == null) {
            // member가 null일 경우 예외 처리
            throw new UsernameNotFoundException("Member not found with email: " + email);
        }
        Long memberId = member.getId();

        try{
            // JPQL을 사용하여 일치하는 모든 NotificationMember 객체의 delYn 값을 YN.Y로 변경
            notificationMemberRepository.updateDelYnByNotificationIdAndMemberId(notificationId, memberId, YN.Y);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 알림 왔는지 확인 용
     *
     * @author 박성민
     */
    public Long searchNotificationsMemberIdReadYn(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return 0L;
        } else {
            String loginId = auth.getName();
            Member member = memberRepository.findByLoginIdAndStatusCode(loginId, MemberStatus.NORMAL);

            return notificationRepository.searchNotificationsMemberIdReadYn(member.getId());
        }
    }
}
