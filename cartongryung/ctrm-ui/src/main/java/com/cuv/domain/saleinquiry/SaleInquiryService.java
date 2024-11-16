package com.cuv.domain.saleinquiry;

import com.cuv.domain.attachment.AttachmentService;
import com.cuv.domain.inquirydetail.InquiryDetailRepository;
import com.cuv.domain.inquirydetail.entity.InquiryDetail;
import com.cuv.domain.inquirydetail.enumset.TradeType;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberRole;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.memberadmin.MemberAdminService;
import com.cuv.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.domain.saleinquiry.dto.SaleInquirySaveDto;
import com.cuv.domain.saleinquiry.entity.SaleInquiry;
import com.cuv.domain.salevehicle.SaleVehicleRepository;
import com.cuv.domain.salevehicle.entity.SaleVehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.util.StringUtils.hasText;

/**
 * 판매 문의 서비스 (문의 등록, 문의 조회)
 */
@Service
@RequiredArgsConstructor
public class SaleInquiryService {

    private final SaleInquiryRepository saleInquiryRepository;
    private final MemberRepository memberRepository;
    private final SaleVehicleRepository saleVehicleRepository;
    private final InquiryDetailRepository inquiryDetailRepository;

    private final MemberAdminService memberAdminService;
    private final AttachmentService attachmentService;

    /**
     * 내 차 팔기 > 사진업로드
     *
     * @param requestDto 등록할 정보가 담긴 DTO
     * @param payload    토큰 정보가 담긴 map
     * @author SungHa
     */
    @Transactional
    public Map<String, Object> saleVehicleSecondStep(SaleInquirySaveDto requestDto, Map<String, Object> payload) {
        if (requestDto.getFileUUIDs().isEmpty())
            throw new IllegalArgumentException("사진은 필수입니다.");

        if (requestDto.getFileUUIDs().size() < 3 || requestDto.getFileUUIDs().size() > 10)
            throw new IllegalArgumentException("사진은 최소 3장, 최대 10장까지 업로드 할 수 있습니다.");

        // 토큰에 차량 정보 담기
        payload = new HashMap<>(payload);
        payload.put("file", requestDto.getFileUUIDs());

        return payload;
    }

    /**
     * 내 차 팔기 > 약관동의
     *
     * @param requestDto 등록할 정보가 담긴 DTO
     * @param payload    토큰 정보가 담긴 map
     * @author SungHa
     */
    @Transactional
    public Map<String, Object> saleVehicleThirdStep(SaleInquirySaveDto requestDto, Map<String, Object> payload) {
        if (!hasText(requestDto.getAgreeUseYn()) || !"on".equals(requestDto.getAgreeUseYn()))
            throw new IllegalArgumentException("개인정보 수집 및 이용에 동의해주세요.");

        // 토큰에 차량 정보 담기
        payload = new HashMap<>(payload);
        payload.put("agree", requestDto.getAgreeUseYn());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        SaleVehicle saleVehicle = SaleVehicle.builder()
                .memberId(member.getId())
                .linkInfoId((Long) payload.get("linkInfoId"))
                .carPlateNumber(String.valueOf(payload.get("vhrno")))
                .ownerName(String.valueOf(payload.get("ownerName")))
                .build();

        saleVehicleRepository.save(saleVehicle);

        // 문의 번호 생성
        LocalDate currentDate = LocalDate.now();
        Random random = new Random();
        int randomNumber = random.nextInt(900) + 100;

        // 딜러 랜덤 배정
        Long memberDealerId = memberAdminService.searchMemberDealerOrderByAssignmentAt(MemberRole.DEALER.getRole());

        SaleInquiry saleInquiry = SaleInquiry.builder()
                .memberId(member.getId())
                .saleVehicleId(saleVehicle.getId())
                .memberDealerId(memberDealerId != null ? memberDealerId : 0L)
                .inquiryNumber("S" + currentDate.format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + randomNumber)
                .attachments(attachmentService.findAllUploadFileDtoByUUID((List<String>) payload.get("file")))
                .build();

        saleInquiryRepository.save(saleInquiry);

        InquiryDetail inquiryDetail = InquiryDetail.builder()
                .inquiryId(saleInquiry.getId())
                .memberAdminId(saleInquiry.getMemberDealerId())
                .tradeTypeCode(TradeType.SELL)
                .consultationStatus(ConsultationStatus.APPLICATION_COMPLETE)
                .content("상담접수완료")
                .build();

        inquiryDetailRepository.save(inquiryDetail);

        return payload;
    }

    /**
     * 내 차 팔기 > 내 차 목록
     *
     * @author SungHa
     */
    public List<SaleInquiryListDto> searchAllSaleInquiryVehicleLists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return saleInquiryRepository.searchAllSaleInquiryVehicleLists(member.getId());
    }

//
    /**
     * 내 차 팔기 > 상세
     *
     * @param id 판매 문의 시퀀스
     * @author SungHa
     */
    public SaleInquiryDetailDto searchSaleInquiryById(Long id) {
        SaleInquiry saleInquiry = saleInquiryRepository.findById(id).orElse(null);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(), MemberStatus.NORMAL);

        // 본인 문의 아니면 튕겨내기
        if (saleInquiry != null && member != null) {
            if (!Objects.equals(saleInquiry.getMemberId(), member.getId())) {
                return null;
            }
        } else {
            return null;
        }

        return saleInquiryRepository.searchSaleInquiryById(id);
    }

    /**
     * 햄버거 메뉴 > 판매 문의 - 방문 예약 수, 내 차 목록 수
     *
     * @author SungHa
     */
    public Long searchSaleInquiryCountByMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return saleInquiryRepository.searchSaleInquiryCountByMemberId(member.getId());
    }

    /**
     * 더보기 > 방문 > 내 차 팔 때
     *
     * @author SungHa
     */
    public List<SaleInquiryListDto> searchAllSaleInquiryLists() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = memberRepository.findByLoginIdAndStatusCode(authentication.getName(),MemberStatus.NORMAL);

        return saleInquiryRepository.searchAllSaleInquiryLists(member.getId());
    }
}
