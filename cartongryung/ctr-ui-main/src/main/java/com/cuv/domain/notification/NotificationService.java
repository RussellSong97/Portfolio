package com.cuv.domain.notification;

import com.cuv.common.YN;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.notification.dto.NotificationListDto;
import com.cuv.domain.notification.entity.Notification;
import com.cuv.domain.pick.entity.Pick;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Component
public class NotificationService {
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    /**
     * 알림 | memberId로 알림들 가져오기
     *
     * @author 박성민
     */
    public List<NotificationListDto> getNotificationsById() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email); // 이메일로 맴버 객체 가져옴
        if (member == null) {
            // member가 null일 경우 예외 처리
            throw new UsernameNotFoundException("Member not found with email: " + email);
        }
        Long memberId = member.getId();

        return notificationRepository.searchNotificationListNoPageList(memberId);

    }

    /**
     * 찜 | getNotificationsById메소드로 가져온 notification들의 readYn 변경
     *
     * @author 박성민
     */
    public void updateReadYn(List<NotificationListDto> notifications) {
        for (NotificationListDto notificationDto : notifications) {
            Notification notification = notificationRepository.findNotificationById(notificationDto.getNotificationId());
            notification.setReadYn(YN.Y);
            notificationRepository.save(notification);
        }
    }

    /**
     * 알림 | 알림 디비에 저장한 거 pickId들로 제거
     *
     * @author 박성민
     */
    public void updateDelYn(Long notificationId) {
        Notification notification = notificationRepository.findNotificationById(notificationId);
        notification.setDelYn(YN.Y);
        notificationRepository.save(notification);
    }

    /**
     * 알림 여부 확인 용
     *
     * @author 박성민
     */
    public Long searchNotificationsMemberIdReadYn(Authentication auth) {
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return 0L;
        } else {
            String loginId = auth.getName();
            Member member = memberRepository.findByEmail(loginId);

            return notificationRepository.searchNotificationsMemberIdReadYn(member.getId());
        }
    }
}