package com.cuv.admin.domain.saleinquiry;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.bizgo.AlimTalkService;
import com.cuv.admin.domain.inquirydetail.dto.InquiryDetailCountDto;
import com.cuv.admin.domain.member.MemberRepository;
import com.cuv.admin.domain.member.entity.Member;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import com.cuv.admin.domain.notification.NotificationRepository;
import com.cuv.admin.domain.notification.entity.Notification;
import com.cuv.admin.domain.notificatonMember.NotificationMemberRepository;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryDetailDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquirySaveDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquirySearchDto;
import com.cuv.admin.domain.saleinquiry.entity.SaleInquiry;
import com.cuv.admin.domain.salevehicle.dto.SaleVehicleDetailDto;
import com.cuv.admin.domain.salevehicle.SaleVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

/**
 * 내 차 팔기 (조회, 삭제, 수정, 저장)
 */
@Service
@RequiredArgsConstructor
public class SaleInquiryService {

    private final SaleInquiryRepository saleInquiryRepository;
    private final SaleVehicleRepository saleVehicleRepository;
    private final MemberAdminRepository memberAdminRepository;
    private final AlimTalkService alimTalkService;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;

    /**
     * 관리자 | 내 차 팔기
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Page<SaleInquiryListDto> searchAllSaleInquiryLists(SaleInquirySearchDto condition, Pageable pageable) {
        return saleInquiryRepository.searchAllSaleInquiryLists(condition, pageable);
    }

    /**
     * 관리자 | 내 차 팔기 > 삭제
     *
     * @param idList 삭제할 시퀀스 값의 배열
     * @author SungHa
     */
    @Transactional
    public void adminSaleInquiryDeleteProc(List<Long> idList) {
        for (Long id : idList) {
            saleInquiryRepository.findById(id).ifPresent(inquiry -> {
                saleVehicleRepository.findById(inquiry.getSaleVehicleId()).ifPresent(vehicle -> {
                    vehicle.setDelYn(YN.Y);
                });
                inquiry.setDelYn(YN.Y);
            });

        }
    }

    /**
     * 관리자 | 내 차 팔기 > 상세
     *
     * @param id 글 시퀀스
     * @author SungHa
     */
    public SaleInquiryDetailDto searchSaleInquiryById(Long id) {
        return saleInquiryRepository.searchSaleInquiryById(id);
    }

    /**
     * 관리자 | 내 차 팔기 > 상세 > 저장
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminSaleInquiryEditProc(SaleInquirySaveDto requestDto) {
        SaleInquiry saleInquiry = saleInquiryRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수가 없습니다."));

        // 배정 딜러 저장
        if (requestDto.getMemberDealerId() != null) {
            MemberAdmin memberAdmin = memberAdminRepository
                    .findById(requestDto.getMemberDealerId())
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수가 없습니다."));

            saleInquiry.setMemberDealerId(memberAdmin.getId());
        }

        // 방문 예약 일시 저장
        if (hasText(requestDto.getDate()) && hasText(requestDto.getMinute()) && hasText(requestDto.getMinute())) {
            String dateTimeString = requestDto.getDate() + "T" + requestDto.getHour() + ":" + requestDto.getMinute();
            LocalDateTime visitReservationAt = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
            saleInquiry.setVisitReservationAt(visitReservationAt);

            // 방문 예약 일시가 새로 설정되었을 때 알림톡 전송
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String visitDateTime = visitReservationAt.format(formatter);

            Optional<Member> member = memberRepository.findById(saleInquiry.getMemberId());
            if (member.isPresent()) {

                Notification notification = Notification.builder()
                        .memberId(member.get().getId())
                        .target("personal")
                        .pushStatus("send")
                        .sendStatus("right")
                        .title(ConsultationStatus.RESERVATION_COMPLETE.getDetail())
                        .content("[내 차 팔기] 안녕하세요. 카통령입니다.\n방문예약 신청이 완료되었습니다. \n\n" +
                                "방문일시 : " + requestDto.getDate() + " " + requestDto.getHour() + "시 " + requestDto.getMinute() +"분\n\n" +
                                "더 나은 중고차 구매가격으로 고객님께 보답하겠습니다.")
                        .readYn(YN.N)
                        .sendAt(LocalDateTime.now())
                        .build();

                notificationRepository.save(notification);

                NotificationMember notificationMember = NotificationMember.builder()
                        .memberId(member.get().getId())
                        .notificationId(notification.getId())
                        .readYn(YN.N)
                        .build();

                notificationMemberRepository.save(notificationMember);

                String mobileNumber = member.get().getMobileNumber();

                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("#{day}", visitDateTime);

                if (YN.Y.equals(member.get().getAgreeNoticeYn())) {
                    alimTalkService.sendAlimTalk("code-09", placeholders, mobileNumber);
                }
            }
        }
    }

    /**
     * 관리자 | 내 차 팔기 > 목록 > 차량 팝업
     *
     * @param id 판매 차량 시퀀스
     * @author SungHa
     */
    public SaleVehicleDetailDto searchSaleVehicleById(Long id) {
        return saleInquiryRepository.searchSaleVehicleById(id);
    }

    /**
     * 관리자 | 메인 > 내 차 팔기
     *
     * @author SungHa
     */
    public Long searchAllSaleInquiryCountByMain() {
        return saleInquiryRepository.searchAllSaleInquiryCountByMain();
    }

    /**
     * 관리자 | 메인 > 내 차 팔기 상담 싱세 수
     *
     * @author SungHa
     */
    public List<InquiryDetailCountDto> searchAllSaleInquiryDetailCountByMain() {
        return saleInquiryRepository.searchAllSaleInquiryDetailCountByMain();
    }

    /**
     * 관리자 | 메인 > 내 차 팔기 > 목록
     *
     * @author SungHa
     */
    public List<SaleInquiryListDto> searchAllSaleInquiryByMain() {
        return saleInquiryRepository.searchAllSaleInquiryByMain();
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Workbook searchAllSaleInquiryExcelLists(SaleInquirySearchDto condition) {
        // 리스트 추출
        List<SaleInquiryListDto> list = saleInquiryRepository.searchAllSaleInquiryExcelLists(condition);

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
        cell.setCellValue("판매 희망 차량");
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
        cell.setCellValue("최근 상담 기록");
        sheet.setColumnWidth(colNo++, 10000);

        // 데이터 부분 생성
        int i = 0;
        for (SaleInquiryListDto vo : list) {

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
            cell.setCellValue(vo.getCarPlateNumber());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getConsultationStatus().getDetail());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getRealName());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getEmail());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getMobileNumber());

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
            cell.setCellValue(vo.getDealer());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getInquiryDetailContent());

        }

        return wb;
    }

}
