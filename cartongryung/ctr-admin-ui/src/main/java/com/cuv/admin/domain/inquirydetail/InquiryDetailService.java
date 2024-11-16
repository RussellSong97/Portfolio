package com.cuv.admin.domain.inquirydetail;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.bizgo.AlimTalkService;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailListDto;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailSaveDto;
import com.cuv.admin.domain.inquirydetail.entity.InquiryDetail;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.linkinfo.LinkInfoRepository;
import com.cuv.admin.domain.linkinfo.entity.LinkInfo;
import com.cuv.admin.domain.member.MemberRepository;
import com.cuv.admin.domain.member.entity.Member;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import com.cuv.admin.domain.notification.NotificationRepository;
import com.cuv.admin.domain.notification.entity.Notification;
import com.cuv.admin.domain.notificatonMember.NotificationMemberRepository;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;
import com.cuv.admin.domain.purchaseinquiry.PurchaseInquiryRepository;
import com.cuv.admin.domain.purchaseinquiry.entity.PurchaseInquiry;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.saleinquiry.SaleInquiryRepository;
import com.cuv.admin.domain.saleinquiry.entity.SaleInquiry;
import com.cuv.admin.domain.salevehicle.SaleVehicleRepository;
import com.cuv.admin.domain.salevehicle.entity.SaleVehicle;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.N;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

/**
 * 상담의 상세정보 관리(상세보기, 상담기록 저장)
 */
@Service
@RequiredArgsConstructor
public class InquiryDetailService {

    private final InquiryDetailRepository inquiryDetailRepository;
    private final MemberAdminRepository memberAdminRepository;
    private final PurchaseInquiryRepository purchaseInquiryRepository;
    private final SaleInquiryRepository saleInquiryRepository;
    private final SaleVehicleRepository saleVehicleRepository;
    private final MemberRepository memberRepository;
    private final LinkInfoRepository linkInfoRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;

    private final AlimTalkService alimTalkService;

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 상담 기록
     * 관리자 | 내 차 팔기 > 연락 가능 문의 > 상세
     * @author SungHa
     */
    public List<InquiryDetailListDto> searchAllInquiryDetailListsByTradeType(Long id, String tradeType) {
        return inquiryDetailRepository.searchAllInquiryDetailListsByTradeType(id, tradeType);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 상담 기록 저장
     * 내 차 팔기 > 상세 > 상담 기록 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminInquiryDetailWriteProc(InquiryDetailSaveDto requestDto) {
        if (!hasText(requestDto.getConsultationStatus()))
            throw new IllegalArgumentException("상담 상태를 선택해주세요.");
        if (!hasText(requestDto.getContent()))
            throw new IllegalArgumentException("상담 기록을 입력해주세요.");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String memberAdminId = auth.getName();
        MemberAdmin memberAdmin = memberAdminRepository
                .findByLoginId(memberAdminId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        InquiryDetail inquiryDetail = InquiryDetail.builder()
                .inquiryId(requestDto.getInquiryId())
                .memberAdminId(memberAdmin.getId())
                .tradeTypeCode(TradeType.ofCode(requestDto.getTradeTypeCode()))
                .consultationStatus(ConsultationStatus.ofCode(requestDto.getConsultationStatus()))
                .content(requestDto.getContent())
                .build();

        inquiryDetailRepository.save(inquiryDetail);

        // 내 차 구입 > 상담 상태가 상담접수완료인 경우 알림톡 전송
        if (ConsultationStatus.APPLICATION_COMPLETE.getCode().equals(requestDto.getConsultationStatus())
                && TradeType.BUY.getCode().equals(requestDto.getTradeTypeCode())) {

            PurchaseInquiry purchaseInquiry = purchaseInquiryRepository.findById(requestDto.getInquiryId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 구매 상담이 없습니다."));

            if(purchaseInquiry.getMemberId() != null) {
                Notification notification = Notification.builder()
                        .memberId(purchaseInquiry.getMemberId())
                        .target("personal")
                        .pushStatus("send")
                        .sendStatus("right")
                        .title(ConsultationStatus.APPLICATION_COMPLETE.getDetail())
                        .content("[내 차 구입] 상담접수가 완료되었습니다.\n 상담도와드릴 수 있도록 빠른 연락 드리겠습니다. \n\n 신뢰하실 수 있는 서비스를 약속드립니다.")
                        .readYn(YN.N)
                        .sendAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);

                NotificationMember notificationMember = NotificationMember.builder()
                        .memberId(purchaseInquiry.getMemberId())
                        .notificationId(notification.getId())
                        .readYn(YN.N)
                        .build();

                notificationMemberRepository.save(notificationMember);
            }

        }

        // 내 차 구입 > 상담 상태가 상담완료인 경우 알림톡 전송
        if (ConsultationStatus.CONSULTATION_COMPLETE.getCode().equals(requestDto.getConsultationStatus())
                && TradeType.BUY.getCode().equals(requestDto.getTradeTypeCode())) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String consultationDateTime = LocalDateTime.now().format(formatter);
            DateTimeFormatter notiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");
            String notiTime = LocalDateTime.now().format(notiFormatter);

            PurchaseInquiry purchaseInquiry = purchaseInquiryRepository.findById(requestDto.getInquiryId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 구매 상담이 없습니다."));

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{day}", consultationDateTime);

            if (purchaseInquiry.getMemberId() != null) {
                // 회원일때 알림톡 전송
                Member member = memberRepository.findById(purchaseInquiry.getMemberId())
                        .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

                Notification notification = Notification.builder()
                        .memberId(member.getId())
                        .target("personal")
                        .pushStatus("send")
                        .sendStatus("right")
                        .title(ConsultationStatus.CONSULTATION_COMPLETE.getDetail())
                        .content("[내 차 구입] 상담이 완료 되었습니다.  \n상담일시 : " + notiTime)
                        .readYn(YN.N)
                        .sendAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);

                NotificationMember notificationMember = NotificationMember.builder()
                        .memberId(member.getId())
                        .notificationId(notification.getId())
                        .readYn(YN.N)
                        .build();

                notificationMemberRepository.save(notificationMember);

                if (YN.Y.equals(member.getAgreeNoticeYn())) {
                    // 수신 동의한 경우에만 알림톡 전송
                    alimTalkService.sendAlimTalk("code-03", placeholders, member.getMobileNumber());
                }
            } else {
                // 비회원일때 알림톡 전송
                alimTalkService.sendAlimTalk("code13", placeholders, purchaseInquiry.getMobileNumber());
            }
        }

        // 내차판매 > 상담 상태가 상담접수완료인 경우 알림톡 전송
        if (ConsultationStatus.APPLICATION_COMPLETE.getCode().equals(requestDto.getConsultationStatus())
                && TradeType.SELL.getCode().equals(requestDto.getTradeTypeCode())) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String applicationDateTime = LocalDateTime.now().format(formatter);
            DateTimeFormatter notiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");
            String notiTime = LocalDateTime.now().format(notiFormatter);

            SaleInquiry saleInquiry = saleInquiryRepository.findById(requestDto.getInquiryId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 판매 상담이 없습니다."));

            Optional<Member> member = memberRepository.findById(saleInquiry.getMemberId());
            if (member.isPresent()) {
                requestDto.setMobileNumber(member.get().getMobileNumber());
                requestDto.setAgreeNoticeYn(member.get().getAgreeNoticeYn());

                Notification notification = Notification.builder()
                        .memberId(saleInquiry.getMemberId())
                        .target("personal")
                        .pushStatus("send")
                        .sendStatus("right")
                        .title(ConsultationStatus.APPLICATION_COMPLETE.getDetail())
                        .content("[내 차 팔기] 차량 상담이 접수 되었습니다.  \n" +
                                "상담일시 : " + notiTime + "\n" +
                                "\n" +
                                "더 나은 중고차 구매가격으로 고객님께 보답하겠습니다.\n")
                        .readYn(YN.N)
                        .sendAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);

                NotificationMember notificationMember = NotificationMember.builder()
                        .memberId(saleInquiry.getMemberId())
                        .notificationId(notification.getId())
                        .readYn(YN.N)
                        .build();

                notificationMemberRepository.save(notificationMember);
            }

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{sellreservationdate}", applicationDateTime);

            if (YN.Y.equals(requestDto.getAgreeNoticeYn())) {
            alimTalkService.sendAlimTalk("code-07", placeholders, requestDto.getMobileNumber());
            }
        }

        // 내차판매 > 상담 상태가 상담완료인 경우 알림톡 전송
        if (ConsultationStatus.CONSULTATION_COMPLETE.getCode().equals(requestDto.getConsultationStatus())
                && TradeType.SELL.getCode().equals(requestDto.getTradeTypeCode())) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String consultationDateTime = LocalDateTime.now().format(formatter);
            DateTimeFormatter notiFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH시 mm분");
            String notiTime = LocalDateTime.now().format(notiFormatter);

            SaleInquiry saleInquiry = saleInquiryRepository.findById(requestDto.getInquiryId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 판매 상담이 없습니다."));

            Optional<Member> member = memberRepository.findById(saleInquiry.getMemberId());
            if (member.isPresent()) {
                requestDto.setMobileNumber(member.get().getMobileNumber());
                requestDto.setAgreeNoticeYn(member.get().getAgreeNoticeYn());

                Notification notification = Notification.builder()
                        .memberId(saleInquiry.getMemberId())
                        .target("personal")
                        .pushStatus("send")
                        .sendStatus("right")
                        .title(ConsultationStatus.CONSULTATION_COMPLETE.getDetail())
                        .content("[내 차 팔기] 상담이 완료 되었습니다.  \n" +
                                "상담일시 : " + notiTime + "\n" +
                                "\n" +
                                "더 나은 중고차 구매가격으로 고객님께 보답하겠습니다.\n")
                        .readYn(YN.N)
                        .sendAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);

                NotificationMember notificationMember = NotificationMember.builder()
                        .memberId(saleInquiry.getMemberId())
                        .notificationId(notification.getId())
                        .readYn(YN.N)
                        .build();

                notificationMemberRepository.save(notificationMember);
            }

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{day}", consultationDateTime);

            if (YN.Y.equals(requestDto.getAgreeNoticeYn())) {
                alimTalkService.sendAlimTalk("code-08", placeholders, requestDto.getMobileNumber());
            }
        }

        // 내차판매 > 상담 상태가 결제완료인 경우 알림톡 전송
        if (ConsultationStatus.PAYMENT_COMPLETE.getCode().equals(requestDto.getConsultationStatus())
                && TradeType.SELL.getCode().equals(requestDto.getTradeTypeCode())) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String paymentDateTime = LocalDateTime.now().format(formatter);

            SaleInquiry saleInquiry = saleInquiryRepository.findById(requestDto.getInquiryId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 판매 상담이 없습니다."));

            Optional<Member> member = memberRepository.findById(saleInquiry.getMemberId());
            if (member.isPresent()) {
                requestDto.setMemberId(member.get().getId());
                requestDto.setMobileNumber(member.get().getMobileNumber());
                requestDto.setRealName(member.get().getRealName());
                requestDto.setAgreeNoticeYn(member.get().getAgreeNoticeYn());
            }

            SaleVehicle saleVehicle = saleVehicleRepository.findById(saleInquiry.getSaleVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 판매 차량이 없습니다."));
            String carPlateNumber = saleVehicle.getCarPlateNumber();

            Notification notification = Notification.builder()
                    .memberId(requestDto.getMemberId())
                    .target("personal")
                    .pushStatus("send")
                    .sendStatus("right")
                    .title(ConsultationStatus.PAYMENT_COMPLETE.getDetail())
                    .content("판매하신 [" + carPlateNumber +"] 차량의 결제가 완료되었습니다. \n" +
                            "\n" +
                            "결제일 : " + paymentDateTime + "\n" +
                            "차량번호 : " + carPlateNumber + "\n" +
                            "\n" +
                            "카통령을 이용해주셔서 감사합니다.")
                    .readYn(YN.N)
                    .sendAt(LocalDateTime.now())
                    .build();

            notificationRepository.save(notification);

            NotificationMember notificationMember = NotificationMember.builder()
                    .memberId(requestDto.getMemberId())
                    .notificationId(notification.getId())
                    .readYn(YN.N)
                    .build();

            notificationMemberRepository.save(notificationMember);

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{day}", paymentDateTime);
            placeholders.put("#{realName}", requestDto.getRealName());
            placeholders.put("#{carPlateNumber}", carPlateNumber);

            if (YN.Y.equals(requestDto.getAgreeNoticeYn())) {
                alimTalkService.sendAlimTalk("code-10", placeholders, requestDto.getMobileNumber());
            }
            // 결제완료 처리
            saleVehicle.setPaymentYn(YN.Y);
//            LinkInfo linkInfo = linkInfoRepository.findById(saleVehicle.getLinkInfoId())
//                    .orElseThrow(() -> new IllegalArgumentException("일치하는 차량 정보가 없습니다."));
//            linkInfo.setDelYn(YN.Y);
        }

        // 승인불가 or 판매취소
        if (ConsultationStatus.DISAPPROVED.getCode().equals(requestDto.getConsultationStatus())
                && TradeType.SELL.getCode().equals(requestDto.getTradeTypeCode()) ||
            ConsultationStatus.CANCEL_SALE.getCode().equals(requestDto.getConsultationStatus())
                    && TradeType.SELL.getCode().equals(requestDto.getTradeTypeCode())) {

            SaleInquiry saleInquiry = saleInquiryRepository.findById(requestDto.getInquiryId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 판매 상담이 없습니다."));
            SaleVehicle saleVehicle = saleVehicleRepository.findById(saleInquiry.getSaleVehicleId())
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 판매 차량이 없습니다."));

            // 결제완료 취소 처리
            saleVehicle.setPaymentYn(YN.N);
//            LinkInfo linkInfo = linkInfoRepository.findById(saleVehicle.getLinkInfoId())
//                    .orElseThrow(() -> new IllegalArgumentException("일치하는 차량 정보가 없습니다."));
//            linkInfo.setDelYn(YN.N);
        }

    }

}
