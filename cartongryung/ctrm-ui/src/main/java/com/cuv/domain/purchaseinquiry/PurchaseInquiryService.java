package com.cuv.domain.purchaseinquiry;

import com.cuv.common.YN;
import com.cuv.domain.bizgo.AlimTalkService;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.notification.NotificationRepository;
import com.cuv.domain.notification.entity.Notification;
import com.cuv.domain.notificationMember.NotificationMemberRepository;
import com.cuv.domain.notificationMember.entity.NotificationMember;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.domain.purchaseinquiry.dto.PurchaseInquirySaveDto;
import com.cuv.domain.purchaseinquiry.entity.PurchaseInquiry;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationType;
import com.cuv.domain.purchaseinquiry.enumset.InquiryType;
import com.cuv.domain.wishlist.WishlistRepository;
import com.cuv.domain.wishlist.entity.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.springframework.util.StringUtils.hasText;

/**
 * 구매 문의 서비스 (문의 등록, 문의 조회)
 */
@Service
@RequiredArgsConstructor
public class PurchaseInquiryService {

    private final PurchaseInquiryRepository purchaseInquiryRepository;
    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;
    private final AlimTalkService alimTalkService;

    /**
     * 상품 상세 > 방문 상담 예약
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void purchaseInquiryWriteProc(PurchaseInquirySaveDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);

        if (!hasText(requestDto.getCalendar()) || !hasText(requestDto.getDay()))
            throw new IllegalArgumentException("상담 날짜를 선택해주세요.");

        if (!hasText(requestDto.getHour()) || !hasText(requestDto.getMinute()))
            throw new IllegalArgumentException("상담 시간을 선택해주세요.");

        // 비회원일 경우
        if (member == null) {
            if (!hasText(requestDto.getRealName()))
                throw new IllegalArgumentException("성함을 입력해주세요.");

            if (!hasText(requestDto.getMobileNumber()))
                throw new IllegalArgumentException("전화번호를 입력해주세요.");

            if (!hasText(requestDto.getUseYn()))
                throw new IllegalArgumentException("개인정보 수집 이용.동의에 체크해주세요.");
        }

        // 회원일 경우 : 마케팅 수신 정보 동의 여부 확인
        if (member != null) {
            String marketingYn = member.getAgreeMarketingYn().toString();
            if (marketingYn.equals("N")) {
                throw new IllegalArgumentException("방문예약 신청하려면 마켓팅 수신 정보 동의가 필요합니다. 동의해 주시길 바랍니다.");
            }
        }

        // 방문 예약 일시 포맷팅
        LocalDateTime visitReservationAt = null;
        if (hasText(requestDto.getCalendar()) && hasText(requestDto.getDay()) && hasText(requestDto.getHour()) && hasText(requestDto.getMinute())) {
            String year = requestDto.getCalendar().split("\\.")[0];
            String month = requestDto.getCalendar().split("\\.")[1];
            String day = String.format("%02d", Integer.parseInt(requestDto.getDay()));
            String date = year + "-" + month + "-" + day;
            String dateTimeString = date + "T" + (requestDto.getHour().length() < 2 ? "0" + requestDto.getHour() : requestDto.getHour()) + ":" + requestDto.getMinute();
            visitReservationAt = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

            if (visitReservationAt.isBefore(LocalDateTime.now()))
                throw new IllegalArgumentException("현재 시간 이후로만 예약이 가능합니다.");
        }

        // 구매 상담 정보
        PurchaseInquiry purchaseInquiry = PurchaseInquiry.builder()
                .memberId(member != null ? member.getId() : null)
                .inquiryTypeCode(InquiryType.VISIT_RESERVATION)
                .consultationTypeCode(ConsultationType.VISIT)
                .realName(member != null ? null : requestDto.getRealName())
                .mobileNumber(member != null ? null : requestDto.getMobileNumber())
                .visitReservationAt(visitReservationAt)
                .build();

        purchaseInquiryRepository.save(purchaseInquiry);

        // 구매 희망 차량
        Wishlist wishlist = Wishlist.builder()
                .purchaseInquiryId(purchaseInquiry.getId())
                .productId(requestDto.getProductId())
                .build();

        wishlistRepository.save(wishlist);

        // 방문 예약 일시가 저장된 후 알림톡 전송
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("#{day}", visitReservationAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        if (purchaseInquiry.getMemberId() != null) {
            // 회원일때 알림톡 전송
            Member memberTalk = memberRepository.findById(purchaseInquiry.getMemberId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

            Notification notification = Notification.builder()
                    .memberId(memberTalk.getId())
                    .target("personal")
                    .pushStatus("send")
                    .sendStatus("right")
                    .title(ConsultationStatus.RESERVATION_COMPLETE.getDetail())
                    .content("[내 차 구입]\n안녕하세요. 카통령입니다.\n방문예약 신청이 완료되었습니다. \n\n" +
                            "방문일시 : " + visitReservationAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분")) + "\n\n" +
                            "신뢰하실 수 있는 서비스를 약속드립니다.")
                    .readYn(YN.N)
                    .sendAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            NotificationMember notificationMember = NotificationMember.builder()
                    .memberId(memberTalk.getId())
                    .notificationId(notification.getId())
                    .readYn(YN.N)
                    .build();

            notificationMemberRepository.save(notificationMember);

            if (YN.Y.equals(memberTalk.getAgreeNoticeYn())) {
                alimTalkService.sendAlimTalk("code-04", placeholders, memberTalk.getMobileNumber());
            }
        } else {
            // 비회원일때 알림톡 전송
            alimTalkService.sendAlimTalk("code14", placeholders, purchaseInquiry.getMobileNumber());
        }
    }

    /**
     * 마케팅 수신 정보 동의 회원
     *
     * @author 송주리
     */
    @Transactional
    public void searchMemberMarketingAgree(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);

        // 회원일 경우 : 마케팅 수신 정보 동의 여부 확인
        if (member != null) {
            String marketingYn = member.getAgreeMarketingYn().toString();
            if (marketingYn.equals("N")) {
                throw new IllegalArgumentException("방문예약 신청하려면 마켓팅 수신 정보 동의가 필요합니다. 동의해 주시길 바랍니다.");
            }
        }
    }

    /**
     * 마케팅 수신 정보 동의 회원 변경
     *
     * @author 송주리
     */
    @Transactional
    public void updateMemberMarketingAgree(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);

        // 회원일 경우 : 마케팅 수신 정보 동의 여부 확인
        if (member != null) {
            member.setAgreeMarketingYn(YN.Y);
            memberRepository.updateAgreeMarketingYnByLoginId(member.getLoginId(), member.getAgreeMarketingYn());
        }else{
            throw new IllegalArgumentException("회원 정보를 알 수 없습니다. 다시 시도해 주시길 바랍니다.");
        }
    }

    /**
     * 햄버거 메뉴 > 상담 수
     *
     * @author SungHa
     */
    public Long searchPurchaseInquiryCountByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return purchaseInquiryRepository.searchPurchaseInquiryCountByMemberId(member.getId());
    }

    /**
     * 햄버거 메뉴 > 구매문의 - 방문 예약 수
     *
     * @author SungHa
     */
    public Long searchVisitReservationCountByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return purchaseInquiryRepository.searchVisitReservationCountByMemberId(member.getId());
    }

    /**
     * 더보기 > 상담
     *
     * @author SungHa
     */
    public List<PurchaseInquiryListDto> searchPurchaseInquiryByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return purchaseInquiryRepository.searchPurchaseInquiryByMemberId(member.getId());
    }

    /**
     * 더보기 > 방문 > 내 차 살 때
     *
     * @author SungHa
     */
    public List<PurchaseInquiryListDto> searchVisitReservationByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return purchaseInquiryRepository.searchVisitReservationByMemberId(member.getId());
    }

    /**
     * aside - 전화상담  - 연락처 없는 문의 등록
     * aside - 채팅상담  - 연락처 없는 문의 등록
     * @param requestDto 판매 문의 정보를 담은 DTO
     * @param consultationTypeCode 상담 구분
     * @author Sangbin
     */
    @Transactional
    public void saveCallPurchaseInquiry(PurchaseInquirySaveDto requestDto, ConsultationType consultationTypeCode) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = null;

        if (authentication != null && authentication.isAuthenticated()) {
            member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);
        }

        InquiryType inquiryType;
        String inquiryNumber = null;

        if (member != null) {
            inquiryType = InquiryType.CONTACTABLE;

            //inquiryNumber(문의 번호) 포맷팅
            LocalDate currentDate = LocalDate.now();
            Random random = new Random();
            int randomNumber = random.nextInt(900) + 100;
            inquiryNumber = "B" + currentDate.format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + randomNumber;
        } else {
            inquiryType = InquiryType.WITHOUT_CONTACT;
        }

        // 판매 문의
        PurchaseInquiry purchaseInquiry = PurchaseInquiry.builder()
                .memberId(member != null ? member.getId() : null)
                .inquiryNumber(inquiryNumber)
                .inquiryTypeCode(inquiryType)
                .consultationTypeCode(consultationTypeCode)
                .realName(requestDto.getRealName())
                .mobileNumber(requestDto.getMobileNumber())
                .connectionIp(requestDto.getConnectionIp())
                .build();

        purchaseInquiryRepository.save(purchaseInquiry);

        // 구매 희망 차량
        if (requestDto.getProductId() != null) {
            Wishlist wishlist = Wishlist.builder()
                    .purchaseInquiryId(purchaseInquiry.getId())
                    .productId(requestDto.getProductId())
                    .build();

            wishlistRepository.save(wishlist);
        }
    }
}
