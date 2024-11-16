package com.cuv.admin.domain.notification;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.AttachmentService;
import com.cuv.admin.domain.boardreview.dto.BoardReviewSaveDto;
import com.cuv.admin.domain.boardreview.entity.BoardReview;
import com.cuv.admin.domain.firebase.FcmService;
import com.cuv.admin.domain.firebase.dto.FcmMultiTokenRequestDto;
import com.cuv.admin.domain.firebase.dto.FcmRequestDto;
import com.cuv.admin.domain.member.MemberService;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import com.cuv.admin.domain.notification.dto.NotificationDetail;
import com.cuv.admin.domain.notification.dto.NotificationListDto;
import com.cuv.admin.domain.notification.dto.NotificationSaveDto;
import com.cuv.admin.domain.notification.dto.NotificationSearchDto;
import com.cuv.admin.domain.notification.entity.Notification;
import com.cuv.admin.domain.notificationIds.NotificationIdsRepository;
import com.cuv.admin.domain.notificationIds.NotificationIdsService;
import com.cuv.admin.domain.notificationIds.entity.NotificationIds;
import com.cuv.admin.domain.notificatonMember.NotificationMemberRepository;
import com.cuv.admin.domain.notificatonMember.NotificationMemberService;
import com.cuv.admin.domain.notificatonMember.dto.NotificationMemberSaveDto;
import com.cuv.admin.domain.notificatonMember.dto.NotificatonMemberMemberIdAndTokenDto;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

/**
 * 알림 (저장, 전송, 조회, 수정)
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AttachmentService attachmentService;
    private final MemberService memberService;
    private final FcmService fcmService;
    private final NotificationMemberService notificationMemberService;

    private final NotificationIdsRepository notificationIdsRepository;
    private final NotificationMemberRepository notificationMemberRepository;

    /**
     * 관리자 | 회원 > 푸시알림 > 푸시알림 리스트
     * @param condition 검색조건
     * @param request 페이징
     * @author 박성민
     */
    public Page<NotificationListDto> searchAllNotificationForListing(NotificationSearchDto condition, PageRequest request) {

        return notificationRepository.searchAllNotificationForListing(condition, request);
    }

    /**
     * 관리자 | 회원 > 푸시알림 > 1) 푸시알림 등록 2) 푸시알림 보낼 맴버(notificationIds) 저장 3) 푸시알림 전송 4) 푸시알림 보내고 알림받은 맴버(notificationMember) 저장
     * @param notificationSaveDto 저장정보
     * @author 박성민
     */
    @Transactional
    public JSONResponse<?> writeAndSendNotification(NotificationSaveDto notificationSaveDto) {

        /**
         * 첨부파일, 링크 없으면 없음 처리하기
         */

        if (notificationSaveDto.getFileUUID() == null) {
            notificationSaveDto.setFileUUID(null);
        }

        if (!hasText(notificationSaveDto.getLinkUrl())) {
            notificationSaveDto.setLinkUrl(null);
        }

        /**
         * 즉시 발송
         */

        String uuid = notificationSaveDto.getFileUUID();

        /**
         * todo: 도메인 이름에 따라 바꿀 것
         */
        String baseUrl = "https://manager.ctrm.co.kr/api/file/view/";

        String imageUrl = baseUrl + uuid +"?size=300";

        Notification notification = null;

        // 이 부분에서 예약 발송을 바꾸기
        // 메시지 저장
        if (notificationSaveDto.getNotificationId() == null) {
            notification = adminNotificationWriteProc(notificationSaveDto);
        } else {
            notification = notificationRepository.findById(notificationSaveDto.getNotificationId()).orElse(null);
        }

        if (notification == null)
            return new JSONResponse<>(500, "알림이 존재하지 않습니다");

        if (notificationSaveDto.getSendStatus().equals("right"))  {
            List<NotificatonMemberMemberIdAndTokenDto> targetMemberIdAndTokenList;

            if (notificationSaveDto.getTarget().equals("marketingAll")){
                // 모든 마케팅 수신 동의 회원 토큰 및 id 가져오기
                targetMemberIdAndTokenList = memberService.findAllMarketingMemberIdAndFcmToken();
            } else if (notificationSaveDto.getTarget().equals("personal")) {
                // 개별 토큰 및 id 가져오기
                targetMemberIdAndTokenList = memberService.findAllMemberIdAndFcmToken(notificationSaveDto.getMemberId());
            }
            else {
                // 모든 토큰 및 id 가져오기
                targetMemberIdAndTokenList = memberService.findMemberIdAndFcmToken();
            }

            // token만 추출하여 리스트로 저장
            List<String> tokenList = targetMemberIdAndTokenList.stream()
                    .map(NotificatonMemberMemberIdAndTokenDto::getToken)
                    .toList();

            boolean isSuccess = true;

            // 에러로그 담을 것
            String errorLog = null;

            // 500개씩 나누어 처리
            int batchSize = 480;

            for (int i = 0; i < tokenList.size(); i += batchSize) {
                List<String> tokenBatch = tokenList.subList(
                        i, Math.min(i + batchSize, tokenList.size()));
                MulticastMessage message = fcmService.setMulticastMessage(
                        notificationSaveDto.getTitle(),
                        notificationSaveDto.getContent(),
                        imageUrl,
                        tokenBatch,
                        notificationSaveDto.getLinkUrl()
                );

                try {
                    // 메시지 전송
                    fcmService.sendMultiMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                    errorLog = e.getMessage();
                }
            }

            if (isSuccess) {
                notificationSaveDto.setFailReason(null);
                notification.setPushStatus("send");

            } else {
                notification.setPushStatus("fail");
                notificationSaveDto.setFailReason(errorLog);
            }

            List<Long> idList = targetMemberIdAndTokenList.stream()
                    .map(NotificatonMemberMemberIdAndTokenDto::getMemberId)
                    .toList();

            for (int i = 0; i < idList.size(); i++) {
                NotificationMember notificationMember = NotificationMember.builder()
                        .memberId(idList.get(i))
                        .notificationId(notification.getId()) // id 불러오기
                        .failReason(notificationSaveDto.getFailReason())
                        .build();

                // 수신인 저장
                notificationMemberService.insertNotificationMember(notificationMember);
            }
        }
        return new JSONResponse<>(200, "SUCCESS");

    }


    /**
     * 관리자 | 회원 > 푸시알림 > 푸시알림 DB에 저장
     *
     * @author 박성민
     * @param requestDto 저장 정보
     */
    public Notification adminNotificationWriteProc(NotificationSaveDto requestDto) {

        if (!requestDto.getTarget().equals("marketingAll") && !requestDto.getTarget().equals("all") && requestDto.getMemberId() == null)
            throw new IllegalArgumentException("보낼 대상을 정해주세요");
        if (!hasText(requestDto.getSendStatus()))
            throw new IllegalArgumentException("발송일시를 정해주세요");
        if (!hasText(requestDto.getTitle()))
            throw new IllegalArgumentException("제목을 넣어주세요");
        if (!hasText(requestDto.getContent()))
            throw new IllegalArgumentException("내용을 넣어주세요");

        // 예약 여부를 확인하기
        Notification notification;

        if (requestDto.getSendStatus().equals("right")){
            notification = Notification.builder()
                    .target(requestDto.getTarget())
                    .pushStatus("stay")
                    .sendStatus(requestDto.getSendStatus())
                    .linkUrl(requestDto.getLinkUrl())
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .attachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()))
                    .reserveAt(requestDto.getLocalDateTime())
                    .sendAt(LocalDateTime.now())
                    .build();
        } else {
            notification = Notification.builder()
                    .target(requestDto.getTarget())
                    .pushStatus("stay")
                    .sendStatus(requestDto.getSendStatus())
                    .linkUrl(requestDto.getLinkUrl())
                    .title(requestDto.getTitle())
                    .content(requestDto.getContent())
                    .attachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()))
                    .reserveAt(requestDto.getLocalDateTime())
                    .sendAt(requestDto.getSendStatus().equals("reserve") ? requestDto.createSendAt(requestDto.getDate(), requestDto.getHour(), requestDto.getMinute()) : LocalDateTime.now())
                    .build();
        }

        try {
            Notification saveNotification = notificationRepository.save(notification);
            if (requestDto.getTarget().equals("personal")) {
                List<NotificationIds> list = new ArrayList<>();
                requestDto.getMemberId().forEach(e -> {
                            list.add(NotificationIds.builder()
                                    .notificationId(saveNotification.getId())
                                    .memberId(e)
                                    .build());
                        }
                );
                if (!list.isEmpty()) notificationIdsRepository.saveAll(list);
            }
            return saveNotification;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 관리자 | 회원 > 푸시알림 > 상세보기
     *
     * @param id 알림 시퀀스
     * @author 박성민
     */
    public NotificationDetail searchNotificationById(Long id) {
        NotificationDetail notificationDetail = notificationRepository.searchNotificationById(id);

        LocalDateTime sendAt = notificationDetail.getSendAt();
        if (notificationDetail.getSendStatus().equals("reserve")) {
            String date = sendAt.toLocalDate().toString(); // Extract the date part as a string
            String hour = String.format("%02d", sendAt.getHour()); // Extract and format the hour part as a string
            String minute = String.format("%02d", sendAt.getMinute()); // Extract and format the minute part as a string

            notificationDetail.setDate(date);
            notificationDetail.setHour(hour);
            notificationDetail.setMinute(minute);
        }
        return notificationDetail;
    }

    /**
     * 관리자 | 회원 > 푸시알림 > 예약 발송할 푸시알림 db에서 가져오기
     *
     * @author 박성민
     *
     */
    public List<NotificationSaveDto> getScheduledNotification() {
        return notificationRepository.getScheduledNotification();
    }


    /**
     * 관리자 | 회원 > 푸시알림 > 수정하기
     *
     * @author 박성민
     * @param notificationSaveDto 저장 정보
     */
    @Transactional
    public JSONResponse<?> editAndSendNotification(NotificationSaveDto notificationSaveDto) {

        Notification notification = notificationRepository.findById(notificationSaveDto.getNotificationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        /**
         * 첨부파일, 링크 없으면 없음 처리하기
         */

        if (notificationSaveDto.getFileUUID() == null) {
            notificationSaveDto.setFileUUID(null);
        }

        if (!hasText(notificationSaveDto.getLinkUrl())) {
            notificationSaveDto.setLinkUrl(null);
        }

        /**
         * 즉시 발송
         */

        String uuid = notificationSaveDto.getFileUUID();

        /**
         * todo: 도메인 이름에 따라 바꿀 것
         */
        String baseUrl = "https://manager.ctrm.co.kr/api/file/view/";

        String imageUrl = baseUrl + uuid + "?size=300";

        // 보낼 사람에서 제외시키기
        if (notification.getTarget().equals("personal")) {
            List<Long> notificationIdsList = notificationIdsRepository.getNotificationIds(notificationSaveDto.getNotificationId());
            notificationIdsRepository.deleteAllById(notificationIdsList);
        }

        // 메시지 수정
        if(notificationSaveDto.getSendStatus().equals("right")) {
            notification.setTarget(notificationSaveDto.getTarget());
            notification.setSendStatus(notificationSaveDto.getSendStatus());
            notification.setPushStatus("right");
            notification.setTitle(notificationSaveDto.getTitle());
            notification.setContent(notificationSaveDto.getContent());
            notification.setLinkUrl(notificationSaveDto.getLinkUrl());
            notification.setAttachment(attachmentService.findUploadFileDtoByUUID(notificationSaveDto.getFileUUID()));
            notification.setSendAt(LocalDateTime.now());
        } else {
            notification.setTarget(notificationSaveDto.getTarget());
            notification.setSendStatus(notificationSaveDto.getSendStatus());
            notification.setPushStatus("stay");
            notification.setTitle(notificationSaveDto.getTitle());
            notification.setContent(notificationSaveDto.getContent());
            notification.setLinkUrl(notificationSaveDto.getLinkUrl());
            notification.setAttachment(attachmentService.findUploadFileDtoByUUID(notificationSaveDto.getFileUUID()));
            notification.setSendAt(notificationSaveDto.getSendStatus().equals("reserve") ? notificationSaveDto.createSendAt(notificationSaveDto.getDate(), notificationSaveDto.getHour(), notificationSaveDto.getMinute()) : LocalDateTime.now());
        }

        // 메시지 저장
        notificationRepository.save(notification);

        // ids db에 ids 저장
        if (notificationSaveDto.getTarget().equals("personal")) {
            List<NotificationIds> list = new ArrayList<>();
            notificationSaveDto.getMemberId().forEach(e -> {
                        list.add(NotificationIds.builder()
                                .notificationId(notification.getId())
                                .memberId(e)
                                .build());
                    }
            );
            if (!list.isEmpty()) notificationIdsRepository.saveAll(list);
        }

        if (notificationSaveDto.getSendStatus().equals("right")) {

            List<NotificatonMemberMemberIdAndTokenDto> targetMemberIdAndTokenList;

            if (notificationSaveDto.getTarget().equals("marketingAll")){
                // 모든 마케팅 수신 동의 회원 토큰 및 id 가져오기
                targetMemberIdAndTokenList = memberService.findAllMarketingMemberIdAndFcmToken();
            } else if (notificationSaveDto.getTarget().equals("personal")) {
                // 개별 토큰 및 id 가져오기
                targetMemberIdAndTokenList = memberService.findAllMemberIdAndFcmToken(notificationSaveDto.getMemberId());
            }
            else {
                // 모든 토큰 및 id 가져오기
                targetMemberIdAndTokenList = memberService.findMemberIdAndFcmToken();
            }

            // token만 추출하여 리스트로 저장
            List<String> tokenList = targetMemberIdAndTokenList.stream()
                    .map(NotificatonMemberMemberIdAndTokenDto::getToken)
                    .toList();

            boolean isSuccess = true;

            // 에러로그 담을 것
            String errorLog = null;

            // 500개씩 나누어 처리
            int batchSize = 480;

            for (int i = 0; i < tokenList.size(); i += batchSize) {
                List<String> tokenBatch = tokenList.subList(
                        i, Math.min(i + batchSize, tokenList.size()));
                MulticastMessage message = fcmService.setMulticastMessage(
                        notificationSaveDto.getTitle(),
                        notificationSaveDto.getContent(),
                        imageUrl,
                        tokenBatch,
                        notificationSaveDto.getLinkUrl()
                );

                try {
                    // 메시지 전송
                    fcmService.sendMultiMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                    isSuccess = false;
                    errorLog = e.getMessage();
                }
            }

            if (isSuccess) {
                notificationSaveDto.setFailReason(null);
                notification.setPushStatus("send");

            } else {
                notification.setPushStatus("fail");
                notificationSaveDto.setFailReason(errorLog);
            }

            // 알림 맴버 소환
            List<Long> ids = notificationMemberRepository.searchNotificationMemberMemberId(notificationSaveDto.getNotificationId());

            // 알림 맴버만 제거
            for (Long id : ids) {
                notificationMemberService.deleteNotificationMemberMemberId(id);  // 각 memberId를 사용하여 삭제
            }

            List<Long> idList = targetMemberIdAndTokenList.stream()
                    .map(NotificatonMemberMemberIdAndTokenDto::getMemberId)
                    .toList();

            for (int i = 0; i < idList.size(); i++) {
                NotificationMember notificationMember = NotificationMember.builder()
                        .memberId(idList.get(i))
                        .notificationId(notification.getId()) // id 불러오기
                        .failReason(notificationSaveDto.getFailReason())
                        .build();

                // 수신인 저장
                notificationMemberService.insertNotificationMember(notificationMember);

            }
        }
        return new JSONResponse<>(200, "SUCCESS");
    }
}
