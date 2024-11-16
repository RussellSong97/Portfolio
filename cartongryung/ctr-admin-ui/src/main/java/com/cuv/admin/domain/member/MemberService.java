package com.cuv.admin.domain.member;

import com.cuv.admin.common.JSONResponse;
import com.cuv.admin.common.YN;
import com.cuv.admin.domain.member.dto.*;
import com.cuv.admin.domain.member.entity.Member;
import com.cuv.admin.domain.member.enumset.MemberStatus;
import com.cuv.admin.domain.notificatonMember.dto.NotificatonMemberMemberIdAndTokenDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquiryListDto;
import com.cuv.admin.domain.purchaseinquiry.dto.PurchaseInquirySearchDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 맴버 관련 정보 및 통계(회원 목록, 상세, 수정, 회원 수, 회원 토큰)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;


    /**
     * 관리자 | 회원 -> 활동회원 목록
     * @param condition 검색 조건
     * @param request 페이지
     * @return
     * @author 송다운
     */
    public Page<MemberListDto> searchAllMember(MemberSearchDto condition, PageRequest request) {

        return memberRepository.searchAllMember(condition, request);
    }


    /**
     * 관리자 | 회원 -> 활동회원 상세
     * @param id 회원 아이디
     * @return
     * @author 송다운
     */
    public MemberDetailDto searchMemberById(Long id) {
        return memberRepository.searchMemberById(id);
    }


    /**
     * 관리자 | 회원 -> 활동회원 상세 -> 회원 정보 업데이트
     * @param dto 회원 정보
     * @return
     * @author 송다운
     */
    public JSONResponse<?> updateMember(MemberSaveDto dto) {
        Member member = memberRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String dbPassword = passwordEncoder.encode(dto.getPassword());

        member.setPassword(dbPassword);
        member.setMobileNumber(dto.getPhone());
        member.setAgreeMarketingYn(YN.ofYn(dto.getMarketingYn()));
        member.setStatusCode(MemberStatus.ofCode(dto.getMemberStatus()));
        if(MemberStatus.ofCode("MST003").equals(member.getStatusCode())) {
            member.setWithdrawAt(LocalDateTime.now());
            member.setWithdrawReason("관리자 탈퇴");
        }

        try {
            memberRepository.save(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONResponse<>(200, "SUCCESS");
    }


    /**
     * 관리자 | 회원 -> 탈퇴회원 목록
     * @param condition 검색 조건
     * @param request 페이지
     * @return
     * @author 송다운
     */
    public Page<MemberListDto> searchAllMemberWithdrawList(MemberSearchDto condition, PageRequest request) {
        return memberRepository.searchAllMemberWithdrawList(condition, request);
    }

    /**
     * 관리자 |회원 -> 탈퇴회원 사유 팝업
     * @param id 회원 아이디
     * @return
     */
    public MemberWithdrawDto searchMemberWithdrawPopup(Long id) {
        return memberRepository.searchMemberWithdrawPopup(id);
    }

    /**
     * 관리자 | 회원 -> 회원 상세 구매 리스트
     * @param id 회원 아이디
     * @return
     */
    public List<MemberPurchaseInquiryListDto> searchMemberPurchaseList(Long id) {
        return memberRepository.searchMemberPurchaseList(id);
    }

    /**
     * 관리자 | 회원 -> 회원 상세 판매 리스트
     * @param id 회원 아이디
     * @return
     */
    public List<MemberSaleInquiryListDto> searchMemberSaleList(Long id) {
        return memberRepository.searchMemberSaleList(id);
    }

    /**
     * 관리자 | 메인 > 회원 현황 > 차트
     *
     * @author SungHa
     */
    public List<MemberJoinCountDto> searchAllWeekJoinMemberCountByMain(String week) {
        return memberRepository.searchAllWeekJoinMemberCountByMain(week);
    }

    /**
     * 관리자 | 메인 > 금일 가입수
     *
     * @author SungHa
     */
    public Long searchAllTodayJoinMemberCountByMain(String today) {
        return memberRepository.searchAllTodayJoinMemberCountByMain(today);
    }

    /**
     * 관리자 | 메인 > 총 회원수
     *
     * @author SungHa
     */
    public Long searchAllMemberCountByMain() {
        return memberRepository.searchAllMemberCountByMain();
    }

    /**
     * 관리자 | 멤버아이디로 토큰 가져오기
     *
     * @author 박성민
     */

    public String findFcmTokenByMemberId(Long memberId){
        return memberRepository.findFcmTokenById(memberId);
    }

    /**
     * 관리자 | 멤버아이디로 토큰 가져오기
     *
     * @author 박성민
     */

    public List<NotificatonMemberMemberIdAndTokenDto> findAllMemberIdAndFcmToken(List<Long> ids){
        return memberRepository.findAllMemberIdAndFcmToken(ids);
    }



    /**
     * 관리자 | 회원 > 활동 화원 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook adminActiveMemberTableExcel(MemberSearchDto condition) {
        // 리스트 추출
        List<MemberListDto> list = memberRepository.adminActiveMemberTableExcel(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("활동 화원");

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
        cell.setCellValue("이름");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("이메일(아이디)");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("휴대폰 번호");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("가입일자");
        sheet.setColumnWidth(colNo++, 3000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("가입 구분");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("최근방문일시");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("내차 구입");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("내차 팔기");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상태");
        sheet.setColumnWidth(colNo++, 3000);

        // 데이터 부분 생성
        int i = 0;
        for (MemberListDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(list.size() - i);
            i++;

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String name = vo.getRealName() != null ? vo.getRealName() : "-";
            cell.setCellValue(name);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String email = vo.getEmail() != null ? vo.getEmail() : "-";
            cell.setCellValue(email);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String mobilNumber = vo.getMobileNumber() != null ? vo.getMobileNumber() : "-";
            cell.setCellValue(mobilNumber);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String createdAt = (vo.getMemberCreatedAt() != null) ? vo.getMemberCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "-";
            cell.setCellValue(createdAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String regCode = null;
            switch (vo.getRegCode().toString()){
                case  "REG001":
                    regCode = "카카오";
                    break;
                case  "REG002":
                    regCode = "네이버";
                    break;
                case  "REG003":
                    regCode = "애플";
                    break;
                case  "REG004":
                    regCode = "구글";
                    break;
                case  "REG005":
                    regCode = "이메일";
                    break;
                default:
                    regCode = "null";
            }
            cell.setCellValue(regCode);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
           String lastLoginAt = (vo.getLastLoginAt() != null) ? vo.getLastLoginAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-";
            cell.setCellValue(lastLoginAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String purchase = vo.getPurchaseCount() > 0 ? "" + vo.getPurchaseCount() : "0";
            cell.setCellValue(purchase);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String sale = vo.getSaleCount() > 0 ? "" + vo.getSaleCount() : "0";
            cell.setCellValue(sale);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String status = vo.getStatusCode() != null ? vo.getStatusCode().getDetail() : "-";
            cell.setCellValue(status);

        }

        return wb;
    }

    /**
     * 관리자 | 회원 > 탈퇴 화원 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook adminWithdrawMemberTableExcel(MemberSearchDto condition) {
        // 리스트 추출
        List<MemberListDto> list = memberRepository.adminWithdrawMemberTableExcel(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("탈퇴 화원");

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
        cell.setCellValue("이름");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("이메일(아이디)");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("휴대폰 번호");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("가입일자");
        sheet.setColumnWidth(colNo++, 3000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("가입 구분");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("최근방문일시");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("내차 구입");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("내차 팔기");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상태");
        sheet.setColumnWidth(colNo++, 3000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("탈퇴사유");
        sheet.setColumnWidth(colNo++, 3000);

        // 데이터 부분 생성
        int i = 0;
        for (MemberListDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(list.size() - i);
            i++;

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String name = vo.getRealName() != null ? vo.getRealName() : "-";
            cell.setCellValue(name);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String email = vo.getEmail() != null ? vo.getEmail() : "-";
            cell.setCellValue(email);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String mobilNumber = vo.getMobileNumber() != null ? vo.getMobileNumber() : "-";
            cell.setCellValue(mobilNumber);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String createdAt = (vo.getMemberCreatedAt() != null) ? vo.getMemberCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "-";
            cell.setCellValue(createdAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String regCode = null;
            switch (vo.getRegCode().toString()){
                case  "REG001":
                    regCode = "카카오";
                    break;
                case  "REG002":
                    regCode = "네이버";
                    break;
                case  "REG003":
                    regCode = "애플";
                    break;
                case  "REG004":
                    regCode = "구글";
                    break;
                case  "REG005":
                    regCode = "이메일";
                    break;
                default:
                    regCode = "null";
            }
            cell.setCellValue(regCode);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String lastLoginAt = (vo.getLastLoginAt() != null) ? vo.getLastLoginAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-";
            cell.setCellValue(lastLoginAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String purchase = vo.getPurchaseCount() > 0 ? "" + vo.getPurchaseCount() : "0";
            cell.setCellValue(purchase);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String sale = vo.getSaleCount() > 0 ? "" + vo.getSaleCount() : "0";
            cell.setCellValue(sale);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String status = vo.getStatusCode() != null ? vo.getStatusCode().getDetail() : "-";
            String withDrawAt = (vo.getWithdrawAt() != null) ? vo.getWithdrawAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "null";
            cell.setCellValue(status + withDrawAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String withdrawReason = vo.getWithdrawReason() != null ? vo.getWithdrawReason() : "";
            cell.setCellValue(withdrawReason);

        }

        return wb;
    }

    /**
     * 관리자 | 회원 푸시 알림 토큰지닌 회원 조회
     * @param condition 검색 조건
     * @param request 페이지
     * @return
     */
    public Page<MemberListDto> searchGetFcmTokenMember(MemberSearchDto condition, PageRequest request) {

        return memberRepository.searchGetFcmTokenMember(condition, request);
    }

    public Page<MemberListDto> searchGetFcmTokenMarketingMember(MemberSearchDto condition, PageRequest request) {

        return memberRepository.searchGetFcmTokenMarketingMember(condition, request);
    }


    /**
     * 관리자 | 회원 푸시 알림 마케팅 수신 가진 회원 리스트
     * @return
     */
    public List<NotificatonMemberMemberIdAndTokenDto> findAllMarketingMemberIdAndFcmToken() {
        return memberRepository.findAllMarketingMemberIdAndFcmToken();
    }

    /**
     * token 있는 회원 조회
     * @return
     */
    public List<NotificatonMemberMemberIdAndTokenDto> findMemberIdAndFcmToken() {
        return memberRepository.findMemberIdAndFcmToken();
    }
}
