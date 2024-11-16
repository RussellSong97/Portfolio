package com.cuv.admin.domain.calendar;

import com.cuv.admin.domain.calendar.dto.*;
import com.cuv.admin.domain.calendar.dto.consult.ConsultDto;
import com.cuv.admin.domain.calendar.dto.consult.ConsultSummaryDto;
import com.cuv.admin.domain.calendar.dto.counslorDealer.CounselorDealerDto;
import com.cuv.admin.domain.calendar.dto.counslorDealer.StatDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatDto;
import com.cuv.admin.domain.calendar.dto.member.MemberStatMonthAndYearDto;
import com.cuv.admin.domain.calendar.entity.Calendar;
import com.cuv.admin.domain.member.enumset.MemberRole;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.MemberAdminRepositoryCustom;
import com.cuv.admin.domain.memberadmin.dto.MemberAdminListDto;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;

/**
 * 통계 조회(회원, 상담사, 딜러, 상담현황 정보 조회 등)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final MemberAdminRepository memberAdminRepository;

    /**
     * 관리자 | 통계 > 회원통계 > 일별
     * @param condition 검색조건
     *
     */
    public List<MemberStatDto> getMemberStat(StatSearchDto condition) {
        List<MemberStatDto> stat = calendarRepository.searchMemberStat(condition);

        return stat;

    }

    /**
     * 관리자 | 통계 > 회원통계 > 월별
     *
     * @param condition 검색조건
     */
    public List<MemberStatMonthAndYearDto> getMemberStatMonth(StatSearchDto condition) {

        List<MemberStatMonthAndYearDto> stat = calendarRepository.searchMemberStatMonth(condition);
        return stat;

    }

    /**
     * 관리자 | 통계 > 회원통계 > 년별
     * @param condition 검색조건
     * @author 박성민
     */
    public List<MemberStatMonthAndYearDto> getMemberStatYear(StatSearchDto condition) {

        List<MemberStatMonthAndYearDto> stat = calendarRepository.searchMemberStatYear(condition);

        return stat;

    }

    /**
     * 관리자 | 통계 > 상담원 > 팔기
     * @param condition 검색조건
     * @author 박성민
     */

    public List<StatDto> getCounselorSaleStat(StatSearchDto condition) {
        List<CounselorDealerDto> counselors = calendarRepository.searchCounselorForSale(condition);
        List<Calendar> calendars = calendarRepository.searchCalendar(condition);

        List<StatDto> result = new ArrayList<>();

        for (Calendar calendar : calendars) {
            LocalDate calendarDate = calendar.getCalendarDate();

            for (CounselorDealerDto counselor : counselors) {

                CounselorDealerDto counselorStat = counselor.getCounselorStat(counselor.getId());

                if (counselor.getDate().equals(calendarDate) && counselorStat != null) {
                    int completedCounselCount = Math.toIntExact(counselorStat.getCompletedCounsel());
                    int completedTradeCount = Math.toIntExact(counselorStat.getCompletedTrade());


                    result.add(new StatDto(
                            calendarDate,
                            counselor.getId(),
                            counselor.getRealName(),
                            completedCounselCount,
                            completedTradeCount
                    ));
                }
            }
        }

        return result;
    }


    /**
     * 관리자 | 통계 > 상담원 > 구입
     * @param condition 검색조건
     * @author 박성민
     */
    public List<StatDto> getCounselorPurchaseStat(StatSearchDto condition) {
        List<CounselorDealerDto> counselors = calendarRepository.searchCounselorForPurchase(condition);
        List<Calendar> calendars = calendarRepository.searchCalendar(condition);


        List<StatDto> result = new ArrayList<>();


        for (Calendar calendar : calendars) {
            LocalDate calendarDate = calendar.getCalendarDate();


            for (CounselorDealerDto counselor : counselors) {

                CounselorDealerDto counselorStat = counselor.getCounselorStat(counselor.getId());

                if (counselor.getDate().equals(calendarDate) && counselorStat != null) {
                    int completedCounselCount = Math.toIntExact(counselorStat.getCompletedCounsel());
                    int completedTradeCount = Math.toIntExact(counselorStat.getCompletedTrade());

                    result.add(new StatDto(
                            calendarDate,
                            counselor.getId(),
                            counselor.getRealName(),
                            completedCounselCount,
                            completedTradeCount
                    ));
                }
            }
        }

        return result;
    }

    /**
     * 관리자 | 통계 > 딜러 > 구입
     * @param condition 검색조건
     * @author 박성민
     */
    public List<StatDto> getDealerPurchaseStat(StatSearchDto condition) {
        List<CounselorDealerDto> dealers = calendarRepository.searchDealerForPurchase(condition);
        List<Calendar> calendars = calendarRepository.searchCalendar(condition);

        // Create a list to hold the result
        List<StatDto> result = new ArrayList<>();

        // Iterate through each calendar date
        for (Calendar calendar : calendars) {
            LocalDate calendarDate = calendar.getCalendarDate();

            // Iterate through each counselor
            for (CounselorDealerDto dealer : dealers) {
                // Filter the stats for the current counselor and date
                CounselorDealerDto counselorStat = dealer.getCounselorStat(dealer.getId());

                if (dealer.getDate().equals(calendarDate) && counselorStat != null) {
                    int completedCounselCount = Math.toIntExact(counselorStat.getCompletedCounsel());
                    int completedTradeCount = Math.toIntExact(counselorStat.getCompletedTrade());

                    // Add an entry for this counselor on this date
                    result.add(new StatDto(
                            calendarDate,
                            dealer.getId(),
                            dealer.getRealName(),
                            completedCounselCount,
                            completedTradeCount
                    ));
                }
            }
        }

        return result;
    }

    /**
     * 관리자 | 통계 > 딜러 > 팔기
     * @param condition 검색조건
     * @author 박성민
     */
    public List<StatDto> getDealerSaleStat(StatSearchDto condition) {
        List<CounselorDealerDto> dealers = calendarRepository.searchDealerForSale(condition);
        List<Calendar> calendars = calendarRepository.searchCalendar(condition);


        List<StatDto> result = new ArrayList<>();


        for (Calendar calendar : calendars) {
            LocalDate calendarDate = calendar.getCalendarDate();


            for (CounselorDealerDto dealer : dealers) {

                CounselorDealerDto counselorStat = dealer.getCounselorStat(dealer.getId());

                if (dealer.getDate().equals(calendarDate) && counselorStat != null) {
                    int completedCounselCount = Math.toIntExact(counselorStat.getCompletedCounsel());
                    int completedTradeCount = Math.toIntExact(counselorStat.getCompletedTrade());


                    result.add(new StatDto(
                            calendarDate,
                            dealer.getId(),
                            dealer.getRealName(),
                            completedCounselCount,
                            completedTradeCount
                    ));
                }
            }
        }

        return result;
    }

    /**
     * 관리자 | 통계 > 상담현황 > 구입
     * @param condition 검색조건
     * @author 박성민
     */
    public List<ConsultDto> searchConsultForPurchase(StatSearchDto condition) {
        return calendarRepository.searchConsultForPurchase(condition);
    }

    /**
     * 관리자 | 통계 > 상담현황 > 판매
     * @param condition 검색조건
     * @author 박성민
     */
    public List<ConsultDto> searchConsultForSale(StatSearchDto condition) {
        return calendarRepository.searchConsultForSale(condition);
    }

    /**
     * 관리자 | 통계 > 상담현황 > 데이터 개수 처리
     * @param consultData  상담현황 데이터
     * @author 박성민
     */
    public ConsultSummaryDto summarizeConsultData(List<ConsultDto> consultData) {
        long totalConsultations = 0;
        long totalVisitReservations = 0;
        long totalRejections = 0;

        for (ConsultDto consult : consultData) {
            totalConsultations += consult.getConsultCountAll();
            totalVisitReservations += consult.getReservationConsultCount();
            totalRejections += consult.getRejectedConsultCount();
        }

        return new ConsultSummaryDto(totalConsultations, totalVisitReservations, totalRejections);
    }

    /**
     * 관리자 | 통계 > 회원 > 일별 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchMemberStatExcel(StatSearchDto condition) {


        // 리스트 추출
        List<MemberStatDto> list = calendarRepository.searchMemberStatExcel(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("회원통계(일별)");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("회원수");
        sheet.setColumnWidth(colNo++, 4000);


        // 데이터 부분 생성
        long totalCount = 0; // 합계를 저장할 변수
        int i = 0;
        for (MemberStatDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);


            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.changeLocalDateToString(vo.getDate()));

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long count = (vo.getCount() != null) ? vo.getCount() : 0L;
            cell.setCellValue(count);
            totalCount += count; // 각 항목의 count를 totalCount에 더함
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        cell = row.createCell(1);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalCount);

        return wb;
    }

    /**
     * 관리자 | 통계 > 회원 > 월별 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchMemberStatMonthExcel(StatSearchDto condition) {

        // 리스트 추출
        List<MemberStatMonthAndYearDto> list = calendarRepository.searchMemberStatMonthExcel(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("회원통계(월별)");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("회원수");
        sheet.setColumnWidth(colNo++, 4000);


        // 데이터 부분 생성
        long totalCount = 0; // 합계를 저장할 변수
        int i = 0;
        for (MemberStatMonthAndYearDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);


            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getDate());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long count = (vo.getCount() != null) ? vo.getCount() : 0L;
            cell.setCellValue(count);
            totalCount += count; // 각 항목의 count를 totalCount에 더함
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        cell = row.createCell(1);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalCount);

        return wb;
    }

    /**
     * 관리자 | 통계 > 회원 > 년별 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchMemberStatYearExcel(StatSearchDto condition) {

        // 리스트 추출
        List<MemberStatMonthAndYearDto> list = calendarRepository.searchMemberStatYearExcel(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("회원통계(년별)");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("회원수");
        sheet.setColumnWidth(colNo++, 4000);


        // 데이터 부분 생성
        long totalCount = 0; // 합계를 저장할 변수
        int i = 0;
        for (MemberStatMonthAndYearDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);


            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getDate());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long count = (vo.getCount() != null) ? vo.getCount() : 0L;
            cell.setCellValue(count);
            totalCount += count; // 각 항목의 count를 totalCount에 더함
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        cell = row.createCell(1);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalCount);

        return wb;
    }

    /**
     * 관리자 | 통계 > 상담원 > 구매 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchCounselorPurchaseExcel(StatSearchDto condition) {

        // 리스트 추출
        List<StatDto> list = getCounselorPurchaseStat(condition);

        list.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberCounselorLists = memberAdminRepository.searchAllMemberCounselor(MemberRole.COUNSELOR.getRole());

        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        list.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> counselorStats = new HashMap<>();
            memberCounselorLists.forEach(counselor ->
                    counselorStats.put(counselor.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, counselorStats);
        });

        for (StatDto stat : list) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("상담원(내차 구입");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

        // 이름
        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        sheet.setColumnWidth(colNo++, 4000);


        // cell 객체를 올바르게 업데이트하지 않아서 발생
        for (int i = 0; i < memberCounselorLists.size(); i++) {
            // 이름 설정
            cell = row.createCell(2 * i + 1);
            cell.setCellStyle(headStyle);
            // 상담원의 이름 설정
            cell.setCellValue(memberCounselorLists.get(i).getRealName());

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2 * i + 1, 2 * i + 2));
        }


        // 상담완료와 결제완료 헤더 행 생성
        Row headerRow = sheet.createRow(rowNo++);
        int headerColNo = 1; // 날짜 열 다음부터 시작

        for (int i = 0; i < memberCounselorLists.size(); i++) {
            // 상담완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("상담완료");

            // 결제완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("결제완료");
        }

        // 데이터 부분 생성
        for (Map.Entry<LocalDate, Map<Long, int[]>> entry : groupedStats.entrySet()) {
            colNo = 0;
            row = sheet.createRow(rowNo++);
            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(entry.getKey().toString());

            for (MemberAdminListDto counselor : memberCounselorLists) {
                int[] stats = entry.getValue().get(counselor.getId());
                if (stats != null) {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[0]);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[1]);
                } else {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);
                }
            }
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo++);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        colNo = 1;
        for (MemberAdminListDto counselor : memberCounselorLists) {
            int totalCompletedCounsel = list.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = list.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedCounsel);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedTrade);
        }

        return wb;
    }

    /**
     * 관리자 | 통계 > 상담원 > 팔기 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchCounselorSaleExcel(StatSearchDto condition) {

        // 리스트 추출
        List<StatDto> list = getCounselorSaleStat(condition);

        list.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberCounselorLists = memberAdminRepository.searchAllMemberCounselor(MemberRole.COUNSELOR.getRole());

        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        list.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> counselorStats = new HashMap<>();
            memberCounselorLists.forEach(counselor ->
                    counselorStats.put(counselor.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, counselorStats);
        });

        for (StatDto stat : list) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("상담원(내차 팔기");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

        // 이름
        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        sheet.setColumnWidth(colNo++, 4000);


        // cell 객체를 올바르게 업데이트하지 않아서 발생
        for (int i = 0; i < memberCounselorLists.size(); i++) {
            // 이름 설정
            cell = row.createCell(2 * i + 1);
            cell.setCellStyle(headStyle);
            // 상담원의 이름 설정
            cell.setCellValue(memberCounselorLists.get(i).getRealName());

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2 * i + 1, 2 * i + 2));
        }


        // 상담완료와 결제완료 헤더 행 생성
        Row headerRow = sheet.createRow(rowNo++);
        int headerColNo = 1; // 날짜 열 다음부터 시작

        for (int i = 0; i < memberCounselorLists.size(); i++) {
            // 상담완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("상담완료");

            // 결제완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("결제완료");
        }

        // 데이터 부분 생성
        for (Map.Entry<LocalDate, Map<Long, int[]>> entry : groupedStats.entrySet()) {
            colNo = 0;
            row = sheet.createRow(rowNo++);
            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(entry.getKey().toString());

            for (MemberAdminListDto counselor : memberCounselorLists) {
                int[] stats = entry.getValue().get(counselor.getId());
                if (stats != null) {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[0]);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[1]);
                } else {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);
                }
            }
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo++);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        colNo = 1;
        for (MemberAdminListDto counselor : memberCounselorLists) {
            int totalCompletedCounsel = list.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = list.stream()
                    .filter(stat -> stat.getId().equals(counselor.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedCounsel);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedTrade);
        }

        return wb;
    }

    /**
     * 관리자 | 통계 > 딜러 > 구입 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchDealerPurchaseExcel(StatSearchDto condition) {

        // 리스트 추출
        List<StatDto> list = getDealerPurchaseStat(condition);

        list.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberDealerLists = memberAdminRepository.searchAllMemberDealer(MemberRole.DEALER.getRole());

        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        list.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> counselorStats = new HashMap<>();
            memberDealerLists.forEach(counselor ->
                    counselorStats.put(counselor.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, counselorStats);
        });


        for (StatDto stat : list) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("딜러(내차 구입");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

        // 이름
        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        sheet.setColumnWidth(colNo++, 4000);


        // cell 객체를 올바르게 업데이트하지 않아서 발생
        for (int i = 0; i < memberDealerLists.size(); i++) {
            // 이름 설정
            cell = row.createCell(2 * i + 1);
            cell.setCellStyle(headStyle);
            // 상담원의 이름 설정
            cell.setCellValue(memberDealerLists.get(i).getRealName());

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2 * i + 1, 2 * i + 2));
        }


        // 상담완료와 결제완료 헤더 행 생성
        Row headerRow = sheet.createRow(rowNo++);
        int headerColNo = 1; // 날짜 열 다음부터 시작

        for (int i = 0; i < memberDealerLists.size(); i++) {
            // 상담완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("상담완료");

            // 결제완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("결제완료");
        }

        // 데이터 부분 생성
        for (Map.Entry<LocalDate, Map<Long, int[]>> entry : groupedStats.entrySet()) {
            colNo = 0;
            row = sheet.createRow(rowNo++);
            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(entry.getKey().toString());

            for (MemberAdminListDto dealer : memberDealerLists) {
                int[] stats = entry.getValue().get(dealer.getId());
                if (stats != null) {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[0]);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[1]);
                } else {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);
                }
            }
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo++);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        colNo = 1;
        for (MemberAdminListDto dealer : memberDealerLists) {
            int totalCompletedCounsel = list.stream()
                    .filter(stat -> stat.getId().equals(dealer.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = list.stream()
                    .filter(stat -> stat.getId().equals(dealer.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedCounsel);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedTrade);
        }

        return wb;
    }

    /**
     * 관리자 | 통계 > 딜러 > 팔기 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchDealerSaleExcel(StatSearchDto condition) {

        // 리스트 추출
        List<StatDto> list = getDealerSaleStat(condition);

        list.sort((a, b) -> b.getCalendarDate().compareTo(a.getCalendarDate()));

        List<MemberAdminListDto> memberDealerLists = memberAdminRepository.searchAllMemberDealer(MemberRole.DEALER.getRole());

        Map<LocalDate, Map<Long, int[]>> groupedStats = new LinkedHashMap<>();

        list.stream().map(StatDto::getCalendarDate).distinct().forEach(date -> {
            Map<Long, int[]> counselorStats = new HashMap<>();
            memberDealerLists.forEach(counselor ->
                    counselorStats.put(counselor.getId(), new int[]{0, 0})
            );
            groupedStats.put(date, counselorStats);
        });

        for (StatDto stat : list) {
            Map<Long, int[]> dateStats = groupedStats.get(stat.getCalendarDate());
            if (dateStats == null) {
                dateStats = new HashMap<>();
                groupedStats.put(stat.getCalendarDate(), dateStats);
            }

            int[] counselorStat = dateStats.get(stat.getId());
            if (counselorStat == null) {
                counselorStat = new int[2];
                dateStats.put(stat.getId(), counselorStat);
            }

            counselorStat[0] = stat.getCompletedCounsel();
            counselorStat[1] = stat.getCompletedTrade();
        }

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("딜러(내차 팔기");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));

        // 이름
        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        sheet.setColumnWidth(colNo++, 4000);


        // cell 객체를 올바르게 업데이트하지 않아서 발생
        for (int i = 0; i < memberDealerLists.size(); i++) {
            // 이름 설정
            cell = row.createCell(2 * i + 1);
            cell.setCellStyle(headStyle);
            // 상담원의 이름 설정
            cell.setCellValue(memberDealerLists.get(i).getRealName());

            sheet.addMergedRegion(new CellRangeAddress(0, 0, 2 * i + 1, 2 * i + 2));
        }


        // 상담완료와 결제완료 헤더 행 생성
        Row headerRow = sheet.createRow(rowNo++);
        int headerColNo = 1; // 날짜 열 다음부터 시작

        for (int i = 0; i < memberDealerLists.size(); i++) {
            // 상담완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("상담완료");

            // 결제완료 셀
            cell = headerRow.createCell(headerColNo++);
            cell.setCellStyle(headStyle);
            cell.setCellValue("결제완료");
        }

        // 데이터 부분 생성
        for (Map.Entry<LocalDate, Map<Long, int[]>> entry : groupedStats.entrySet()) {
            colNo = 0;
            row = sheet.createRow(rowNo++);
            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(entry.getKey().toString());

            for (MemberAdminListDto dealer : memberDealerLists) {
                int[] stats = entry.getValue().get(dealer.getId());
                if (stats != null) {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[0]);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(stats[1]);
                } else {
                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);

                    cell = row.createCell(colNo++);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(0);
                }
            }
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo++);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        colNo = 1;
        for (MemberAdminListDto dealer : memberDealerLists) {
            int totalCompletedCounsel = list.stream()
                    .filter(stat -> stat.getId().equals(dealer.getId()))
                    .mapToInt(StatDto::getCompletedCounsel)
                    .sum();
            int totalCompletedTrade = list.stream()
                    .filter(stat -> stat.getId().equals(dealer.getId()))
                    .mapToInt(StatDto::getCompletedTrade)
                    .sum();

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedCounsel);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(totalCompletedTrade);
        }

        return wb;
    }

    /**
     * 관리자 | 통계 > 상담현황 > 구매 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchConsultPurchaseExcel(StatSearchDto condition) {

        // 리스트 추출
        List<ConsultDto> list = searchConsultForPurchase(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("상담현황(내차 구입");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상담신청");
        sheet.setColumnWidth(colNo++, 4000);


        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("방문예약 신청");
        sheet.setColumnWidth(colNo++, 4000);


        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("승인불가");
        sheet.setColumnWidth(colNo++, 4000);

        // 데이터 부분 생성
        long totalCount = 0; // 합계를 저장할 변수
        long totalReservation = 0;
        long totalRejected = 0;

        int i = 0;
        for (ConsultDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.changeLocalDateToString(vo.getDate()));

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long count = (vo.getConsultCountAll() != null) ? vo.getConsultCountAll() : 0L;
            cell.setCellValue(count);
            totalCount += count; // 각 항목의 count를 totalCount에 더함

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long reservationCount = (vo.getReservationConsultCount() != null) ? vo.getReservationConsultCount() : 0L;
            cell.setCellValue(reservationCount);
            totalReservation += reservationCount; // 각 항목의 count를 totalCount에 더함

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long rejectedCount = (vo.getRejectedConsultCount() != null) ? vo.getRejectedConsultCount() : 0L;
            cell.setCellValue(rejectedCount);
            totalRejected += rejectedCount; // 각 항목의 count를 totalCount에 더함
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        cell = row.createCell(1);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalCount);

        cell = row.createCell(2);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalReservation);

        cell = row.createCell(3);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalRejected);

        return wb;
    }

    /**
     * 관리자 | 통계 > 상담현황 > 판매 > 엑셀다운
     *
     * @param condition 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Workbook searchConsultSaleExcel(StatSearchDto condition) {

        // 리스트 추출
        List<ConsultDto> list = searchConsultForSale(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("상담현황(내차 팔기");

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
        cell.setCellValue("날짜");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상담신청");
        sheet.setColumnWidth(colNo++, 4000);


        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("방문예약 신청");
        sheet.setColumnWidth(colNo++, 4000);


        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("승인불가");
        sheet.setColumnWidth(colNo++, 4000);

        // 데이터 부분 생성
        long totalCount = 0; // 합계를 저장할 변수
        long totalReservation = 0;
        long totalRejected = 0;

        int i = 0;
        for (ConsultDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.changeLocalDateToString(vo.getDate()));

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long count = (vo.getConsultCountAll() != null) ? vo.getConsultCountAll() : 0L;
            cell.setCellValue(count);
            totalCount += count; // 각 항목의 count를 totalCount에 더함

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long reservationCount = (vo.getReservationConsultCount() != null) ? vo.getReservationConsultCount() : 0L;
            cell.setCellValue(reservationCount);
            totalReservation += reservationCount; // 각 항목의 count를 totalCount에 더함

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            Long rejectedCount = (vo.getRejectedConsultCount() != null) ? vo.getRejectedConsultCount() : 0L;
            cell.setCellValue(rejectedCount);
            totalRejected += rejectedCount; // 각 항목의 count를 totalCount에 더함
        }

        // 합계 행 추가
        row = sheet.createRow(rowNo);

        cell = row.createCell(0);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue("합계");

        cell = row.createCell(1);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalCount);

        cell = row.createCell(2);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalReservation);

        cell = row.createCell(3);
        cell.setCellStyle(bodyStyle);
        cell.setCellValue(totalRejected);

        return wb;
    }

}
