package com.cuv.admin.domain.purchaseinquiry;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.bizgo.AlimTalkService;
import com.cuv.admin.domain.inquirydetail.InquiryDetailRepository;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailCountDto;
import com.cuv.admin.domain.inquirydetail.entity.InquiryDetail;
import com.cuv.admin.domain.inquirydetail.enumset.TradeType;
import com.cuv.admin.domain.member.MemberRepository;
import com.cuv.admin.domain.member.entity.Member;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import com.cuv.admin.domain.notification.NotificationRepository;
import com.cuv.admin.domain.notification.entity.Notification;
import com.cuv.admin.domain.notificatonMember.NotificationMemberRepository;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;
import com.cuv.admin.domain.purchaseinquiry.dto.*;
import com.cuv.admin.domain.purchaseinquiry.entity.PurchaseInquiry;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationType;
import com.cuv.admin.domain.purchaseinquiry.enumset.InquiryType;
import com.cuv.admin.domain.wishlist.WishlistRepository;
import com.cuv.admin.domain.wishlist.entity.Wishlist;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.util.StringUtils.hasText;

/**
 * 내 차 구입 문의 (저장, 수정, 삭제, 문의 이동 등)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PurchaseInquiryService {

    private final PurchaseInquiryRepository purchaseInquiryRepository;
    private final InquiryDetailRepository inquiryDetailRepository;
    private final MemberRepository memberRepository;
    private final MemberAdminRepository memberAdminRepository;
    private final WishlistRepository wishlistRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;

    private final AlimTalkService alimTalkService;

    /**
     * 관리자 | 내 차 구입 > 문의 번호 생성
     *
     * @author SungHa
     */
    public static String createInquiryNumber() {
        LocalDate currentDate = LocalDate.now();
        Random random = new Random();
        int randomNumber = random.nextInt(900) + 100;

        return "B" + currentDate.format(DateTimeFormatter.ofPattern("yyMMdd")) + "-" + randomNumber;
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의, 방문 예약 > 목록
     *
     * @param condition 상담 구분을 담은 DTO
     * @author SungHa
     */
    public Page<PurchaseInquiryListDto> searchAllPurchaseInquiryLists(PurchaseInquirySearchDto condition, Pageable pageable) {
        return purchaseInquiryRepository.searchAllPurchaseInquiryLists(condition, pageable);
    }

    /**
     * 관리자 | 내 차 구입 > 연락처 없는 문의 > 목록
     *
     * @param condition 상담 구분을 담은 DTO
     * @author SungHa
     */
    public Page<PurchaseInquiryListDto> searchAllWithoutContactInquiryLists(PurchaseInquirySearchDto condition, Pageable pageable) {
        return purchaseInquiryRepository.searchAllWithoutContactInquiryLists(condition, pageable);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의, 연락처 없는 문의, 방문 예약 > 개수
     *
     * @author SungHa
     */
    public List<Long> searchAllPurchaseInquiryCount() {
        return purchaseInquiryRepository.searchAllPurchaseInquiryCount();
    }

    /**
     * 관리자 | 내 차 구입 > 연락처 없는 문의, 방문 예약 > 연락 가능 문의 이동
     *
     * @param idList 이동할 시퀀스 값의 배열
     * @author SungHa
     */
    @Transactional
    public void adminPurchaseInquiryMove(List<Long> idList) {
        for (Long id : idList) {
            PurchaseInquiry purchaseInquiry = purchaseInquiryRepository
                    .findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 문의입니다."));

            purchaseInquiry.setInquiryNumber(createInquiryNumber());
            purchaseInquiry.setInquiryTypeCode(InquiryType.CONTACTABLE);
        }
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의, 연락처 없는 문의, 방문 예약 > 삭제
     *
     * @param idList 삭제할 시퀀스 값의 배열
     * @author SungHa
     */
    @Transactional
    public void adminPurchaseInquiryDeleteProc(List<Long> idList) {
        for (Long id : idList) {
            purchaseInquiryRepository.findById(id).ifPresent(inquiry -> inquiry.setDelYn(YN.Y));
        }
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 등록 > 팝업
     *
     * @param inquiryId 상담 시퀀스
     * @author SungHa
     */
    public PurchaseInquiryMemberDto searchMemberInfoByPurchaseInquiryId(Long inquiryId) {
        return purchaseInquiryRepository.searchMemberInfoByPurchaseInquiryId(inquiryId);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 등록 > 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminPurchaseMemberWriteProc(PurchaseInquiryMemberDto requestDto) {
        if (requestDto.getMemberId() == null) {
            if (!hasText(requestDto.getRealName()))
                throw new IllegalArgumentException("이름을 입력해주세요.");
            if (!hasText(requestDto.getEmail()))
                throw new IllegalArgumentException("이메일을 입력해주세요.");
            if (!hasText(requestDto.getMobileNumber()))
                throw new IllegalArgumentException("휴대폰 번호를 입력해주세요.");
        }

        if (!hasText(requestDto.getSaveConsultationTypeCode()))
            throw new IllegalArgumentException("상담 구분을 선택해주세요.");

        // 기존 문의 수정
        if (requestDto.getId() != null) {
            PurchaseInquiry purchaseInquiry = purchaseInquiryRepository
                    .findById(requestDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수가 없습니다."));

            // 회원 조회 후 저장
            if (requestDto.getMemberId() != null) {
                purchaseInquiry.setMemberId(requestDto.getMemberId());
                purchaseInquiry.setConsultationTypeCode(ConsultationType.ofCode(requestDto.getSaveConsultationTypeCode()));
            } else {
                // 회원 정보 직접 입력
                Optional<Member> existingMember = memberRepository.findByMobileNumber(requestDto.getMobileNumber());
                if (existingMember.isPresent())
                    throw new IllegalArgumentException("이미 존재하는 회원입니다.");

                purchaseInquiry.setRealName(requestDto.getRealName());
                purchaseInquiry.setEmail(requestDto.getEmail());
                purchaseInquiry.setMobileNumber(requestDto.getMobileNumber());
                purchaseInquiry.setConsultationTypeCode(ConsultationType.ofCode(requestDto.getSaveConsultationTypeCode()));
            }

        } else {
            // 새로운 문의 생성
            // 회원 조회 후 저장
            PurchaseInquiry purchaseInquiry;
            if (requestDto.getMemberId() != null) {
                // 알림톡 전송 위해 회원 조회 후 문의 등록 시 mobileNumber값 테이블에 저장
                Optional<Member> member = memberRepository.findById(requestDto.getMemberId());
                member.ifPresent(value -> requestDto.setMobileNumber(value.getMobileNumber()));

                purchaseInquiry = PurchaseInquiry.builder()
                        .memberId(requestDto.getMemberId())
                        .inquiryTypeCode(InquiryType.CONTACTABLE)
                        .consultationTypeCode(ConsultationType.ofCode(requestDto.getSaveConsultationTypeCode()))
                        .inquiryNumber(createInquiryNumber())
                        .mobileNumber(requestDto.getMobileNumber())
                        .build();

                purchaseInquiryRepository.save(purchaseInquiry);
            } else {
                // 회원 정보 직접 입력
                Optional<Member> existingMember = memberRepository.findByMobileNumber(requestDto.getMobileNumber());
                if (existingMember.isPresent())
                    throw new IllegalArgumentException("이미 존재하는 회원입니다. 회원 조회 후 선택해주세요.");

                purchaseInquiry = PurchaseInquiry.builder()
                        .realName(requestDto.getRealName())
                        .email(requestDto.getEmail())
                        .mobileNumber(requestDto.getMobileNumber())
                        .inquiryTypeCode(InquiryType.CONTACTABLE)
                        .consultationTypeCode(ConsultationType.ofCode(requestDto.getSaveConsultationTypeCode()))
                        .inquiryNumber(createInquiryNumber())
                        .build();

                purchaseInquiryRepository.save(purchaseInquiry);
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String memberAdminId = auth.getName();
            MemberAdmin memberAdmin = memberAdminRepository
                    .findByLoginId(memberAdminId)
                    .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

            InquiryDetail inquiryDetail = InquiryDetail.builder()
                    .inquiryId(purchaseInquiry.getId())
                    .memberAdminId(memberAdmin.getId())
                    .tradeTypeCode(TradeType.BUY)
                    .consultationStatus(ConsultationStatus.APPLICATION_COMPLETE)
                    .content("상담접수완료")
                    .build();

            inquiryDetailRepository.save(inquiryDetail);

        }
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    public PurchaseInquiryDetailDto searchPurchaseInquiryById(Long id) {
        return purchaseInquiryRepository.searchPurchaseInquiryById(id);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 저장
     * 내 차 팔기 > 상세 > 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminPurchaseInquiryEditProc(PurchaseInquirySaveDto requestDto) {
        if (!hasText(requestDto.getSaveConsultationTypeCode()))
            throw new IllegalArgumentException("상담 구분을 선택해주세요.");

        PurchaseInquiry purchaseInquiry = purchaseInquiryRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수가 없습니다."));

        purchaseInquiry.setConsultationTypeCode(ConsultationType.ofCode(requestDto.getSaveConsultationTypeCode()));

        // 방문 예약 일시 저장
        if (hasText(requestDto.getDate()) && hasText(requestDto.getHour()) && hasText(requestDto.getMinute())) {
            String dateTimeString = requestDto.getDate() + "T" + requestDto.getHour() + ":" + requestDto.getMinute();
            LocalDateTime visitReservationAt = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            purchaseInquiry.setVisitReservationAt(visitReservationAt);

            // 방문 예약 일시가 저장된 후 알림톡 전송
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("#{day}", visitReservationAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

            if (purchaseInquiry.getMemberId() != null) {
                // 회원일때 알림톡 전송
                Member member = memberRepository.findById(purchaseInquiry.getMemberId())
                        .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

                Notification notification = Notification.builder()
                        .memberId(member.getId())
                        .target("personal")
                        .pushStatus("send")
                        .sendStatus("right")
                        .title(ConsultationStatus.RESERVATION_COMPLETE.getDetail())
                        .content("[내 차 구입]\n안녕하세요. 카통령입니다.\n방문예약 신청이 완료되었습니다. \n\n" +
                                "방문일시 : " + requestDto.getDate() + " " + requestDto.getHour() + "시 " + requestDto.getMinute() +"분\n\n" +
                                "신뢰하실 수 있는 서비스를 약속드립니다.")
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
                    alimTalkService.sendAlimTalk("code-04", placeholders, member.getMobileNumber());
                }
            } else {
                // 비회원일때 알림톡 전송
                alimTalkService.sendAlimTalk("code14", placeholders, purchaseInquiry.getMobileNumber());
            }
        }

        // 기존 구매희망 차량번호 삭제
        List<Wishlist> wishlist = wishlistRepository.findByPurchaseInquiryId(requestDto.getId()).orElse(null);
        if (wishlist != null) {
            for (Wishlist wish : wishlist) {
                wish.setDelYn(YN.Y);
            }
        }

        // 구매희망 차량번호 추가
        if (requestDto.getProductId() != null) {
            List<Wishlist> newWishlists = new ArrayList<>();
            for (Long productId : requestDto.getProductId()) {
                Wishlist newWishlist = Wishlist.builder()
                        .purchaseInquiryId(requestDto.getId())
                        .productId(productId)
                        .build();
                newWishlists.add(newWishlist);
            }

            wishlistRepository.saveAll(newWishlists);
        }
    }

    /**
     * 관리자 | 메인 > 내 차 구입 수
     *
     * @author SungHa
     */
    public Long searchAllPurchaseInquiryCountByMain() {
        return purchaseInquiryRepository.searchAllPurchaseInquiryCountByMain();
    }

    /**
     * 관리자 | 메인 > 내 차 구입 상담 상세 수
     *
     * @author SungHa
     */
    public List<InquiryDetailCountDto> searchAllPurchaseInquiryDetailCountByMain() {
        return purchaseInquiryRepository.searchAllPurchaseInquiryDetailCountByMain();
    }

    /**
     * 관리자 | 메인 > 내 차 구입, 방문 예약 목록
     *
     * @author SungHa
     */
    public List<PurchaseInquiryListDto> searchAllPurchaseInquiryByMain(PurchaseInquirySearchDto condition) {
        return purchaseInquiryRepository.searchAllPurchaseInquiryByMain(condition);
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Workbook searchAllContactableInquiryExcelLists(PurchaseInquirySearchDto condition) {
        // 리스트 추출
        List<PurchaseInquiryListDto> list = purchaseInquiryRepository.searchAllContactableInquiryExcelLists(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("연락 가능 문의");

        // 테이블 헤더용 스타일
        CellStyle headStyle = wb.createCellStyle();

        // 가는 경계선 지정
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 배경 그레이 색
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 데이터는 가운데 정렬
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        // 데이터용 경계 스타일 테두리만 지정
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row row;
        Cell cell;
        int rowNo = 0;
        int colNo = 0;

        // 헤더 생성
        row = sheet.createRow(rowNo++);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("번호");
        sheet.setColumnWidth(colNo++, 2000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("문의번호");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("구매 희망 차량");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상담 구분");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상태");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("회원명");
        sheet.setColumnWidth(colNo++, 3000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("이메일");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("휴대폰번호");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("방문 예약 일시");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("등록일시");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("배정 딜러");
        sheet.setColumnWidth(colNo++, 3000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상담원");
        sheet.setColumnWidth(colNo++, 3000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("최근 상담 기록");
        sheet.setColumnWidth(colNo++, 10000);

        // 데이터 부분 생성
        int i = 0;
        for (PurchaseInquiryListDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(list.size() - i);
            i++;

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getInquiryNumber());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String carPlateNumber = vo.getCarPlateNumber() != null ? vo.getCarPlateNumber() : "-";
            String wishCount = vo.getWishCount() > 1 ? " 외 " + (vo.getWishCount() - 1) : "";
            cell.setCellValue(carPlateNumber + wishCount);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getConsultationTypeCode().getDetail());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getConsultationStatus().getDetail());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String realName = (vo.getMemberId() == null) ? vo.getRealName() : vo.getMemberRealName();
            cell.setCellValue(realName);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String email = (vo.getMemberId() == null) ? vo.getEmail() : vo.getMemberEmail();
            cell.setCellValue(email);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String mobileNumber = (vo.getMemberId() == null) ? vo.getMobileNumber() : vo.getMemberMobileNumber();
            cell.setCellValue(mobileNumber);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String visitReservationAt = (vo.getVisitReservationAt() != null) ? vo.getVisitReservationAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "-";
            cell.setCellValue(visitReservationAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String createdAt = (vo.getCreatedAt() != null) ? vo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-";
            cell.setCellValue(createdAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String dealer = vo.getDealer() != null ? vo.getDealer() : "-";
            String dealerCount = vo.getDealerCount() > 1 ? " 외 " + (vo.getDealerCount() - 1) : "";
            cell.setCellValue(dealer + dealerCount);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getInquiryDetailWriter());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getInquiryDetailContent());
        }

        return wb;
    }

    /**
     * 관리자 | 내 차 구입 > 연락처 없는 문의 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Workbook searchAllWithoutContactInquiryExcelLists(PurchaseInquirySearchDto condition) {
        // 리스트 추출
        List<PurchaseInquiryListDto> list = purchaseInquiryRepository.searchAllWithoutContactInquiryExcelLists(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("연락처 없는 문의");

        // 테이블 헤더용 스타일
        CellStyle headStyle = wb.createCellStyle();

        // 가는 경계선 지정
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 배경 그레이 색
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 데이터는 가운데 정렬
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        // 데이터용 경계 스타일 테두리만 지정
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row row;
        Cell cell;
        int rowNo = 0;
        int colNo = 0;

        // 헤더 생성
        row = sheet.createRow(rowNo++);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("번호");
        sheet.setColumnWidth(colNo++, 2000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("구매 희망 차량");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상담 구분");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("접속 아이피");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("등록일시");
        sheet.setColumnWidth(colNo++, 6000);

        // 데이터 부분 생성
        int i = 0;
        for (PurchaseInquiryListDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(list.size() - i);
            i++;

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getCarPlateNumber());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getConsultationTypeCode().getDetail());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getConnectionIp());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String createdAt = (vo.getCreatedAt() != null) ? vo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-";
            cell.setCellValue(createdAt);

        }

        return wb;
    }

    /**
     * 관리자 | 내 차 구입 > 방문 예약 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author Sangbin
     */
    public Workbook searchAllVisitInquiryExcelLists(PurchaseInquirySearchDto condition) {
        // 리스트 추출
        List<PurchaseInquiryListDto> list = purchaseInquiryRepository.searchAllVisitInquiryExcelLists(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("방문 예약");

        // 테이블 헤더용 스타일
        CellStyle headStyle = wb.createCellStyle();

        // 가는 경계선 지정
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);

        // 배경 그레이 색
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 데이터는 가운데 정렬
        headStyle.setAlignment(HorizontalAlignment.CENTER);

        // 데이터용 경계 스타일 테두리만 지정
        CellStyle bodyStyle = wb.createCellStyle();
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);
        bodyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        Row row;
        Cell cell;
        int rowNo = 0;
        int colNo = 0;

        // 헤더 생성
        row = sheet.createRow(rowNo++);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("번호");
        sheet.setColumnWidth(colNo++, 2000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("구분");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("이름");
        sheet.setColumnWidth(colNo++, 3000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("차량번호");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("휴대폰번호");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("이메일");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("방문 예약일시");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("등록일시");
        sheet.setColumnWidth(colNo++, 6000);

        // 데이터 부분 생성
        int i = 0;
        for (PurchaseInquiryListDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(list.size() - i);
            i++;

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getUserType());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String realName = (vo.getUserType().equals("회원") && vo.getMemberRealName() != null) ? vo.getMemberRealName() : vo.getRealName();
            cell.setCellValue(realName);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String carPlateNumber = vo.getCarPlateNumber() != null ? vo.getCarPlateNumber() : "-";
            String wishCount = vo.getWishCount() > 1 ? " 외 " + (vo.getWishCount() - 1) : "";
            cell.setCellValue(carPlateNumber + wishCount);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String mobileNumber = (vo.getUserType().equals("회원") && vo.getMemberMobileNumber() != null) ? vo.getMemberMobileNumber() : vo.getMobileNumber();
            cell.setCellValue(mobileNumber);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String email = (vo.getUserType().equals("회원") && vo.getMemberEmail() != null) ? vo.getMemberEmail() : vo.getEmail();
            cell.setCellValue(email);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String visitReservationAt = (vo.getVisitReservationAt() != null) ? vo.getVisitReservationAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "-";
            cell.setCellValue(visitReservationAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String createdAt = (vo.getCreatedAt() != null) ? vo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-";
            cell.setCellValue(createdAt);

        }

        return wb;
    }
}
