package com.cuv.admin.domain.product;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.bizgo.AlimTalkService;
import com.cuv.admin.domain.linkinfo.LinkInfoRepository;
import com.cuv.admin.domain.linkinfo.entity.LinkInfo;
import com.cuv.admin.domain.member.MemberRepository;
import com.cuv.admin.domain.member.entity.Member;
import com.cuv.admin.domain.notification.NotificationRepository;
import com.cuv.admin.domain.notification.entity.Notification;
import com.cuv.admin.domain.notificatonMember.NotificationMemberRepository;
import com.cuv.admin.domain.notificatonMember.entity.NotificationMember;
import com.cuv.admin.domain.product.dto.ProductListDto;
import com.cuv.admin.domain.product.dto.ProductSaveDto;
import com.cuv.admin.domain.product.dto.ProductSearchDto;
import com.cuv.admin.domain.product.entity.Product;
import com.cuv.admin.domain.product.enumset.PostStatus;
import com.cuv.admin.domain.purchaseinquiry.PurchaseInquiryRepository;
import com.cuv.admin.domain.purchaseinquiry.entity.PurchaseInquiry;
import com.cuv.admin.domain.purchaseinquiry.enumset.ConsultationStatus;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquiryListDto;
import com.cuv.admin.domain.saleinquiry.dto.SaleInquirySearchDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 차량관련 목록, 통계, 팝업 등
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PurchaseInquiryRepository purchaseInquiryRepository;
    private final MemberRepository memberRepository;
    private final LinkInfoRepository linkInfoRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMemberRepository notificationMemberRepository;

    private final AlimTalkService alimTalkService;

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 상세 > 차량 조회 목록
     *
     * @author SungHa
     */
    public Page<ProductListDto> searchAllPostingProductLists(ProductSearchDto condition, Pageable pageable) {
        return productRepository.searchAllPostingProductLists(condition, pageable);
    }

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 목록
     * 관리자 | 차량 관리 > 추천 차량 > 목록
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Page<ProductListDto> searchAllProduct(ProductSearchDto condition, Pageable pageable) {
        return productRepository.searchAllProduct(condition, pageable);
    }

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 목록 - 판매 노출 상태 변경: 게시, 판매완료
     * 관리자 | 내 차 구입 > 상세 > 판매 완료
     *
     * @param requestDto 상태값을 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminManagementProductChange(ProductSaveDto requestDto) {
        List<Product> productLists = productRepository.findAllById(requestDto.getId());

        productLists.forEach(product -> {
            // 판매 완료 상태에서의 상태 변경을 방지
            if (PostStatus.SOLDOUT.equals(product.getPostStatus())) {
                throw new IllegalArgumentException("판매완료에서는 상태를 바꿀 수 없습니다.");
            }

            if (PostStatus.POST.getCode().equals(requestDto.getPostStatus())) {
                product.setPostStatus(PostStatus.POST);
                product.setSoldOutAt(null); // 판매 완료 시간 초기화 (POST 상태로 변경)
            } else {
                product.setPostStatus(PostStatus.SOLDOUT);
                product.setSoldOutAt(LocalDateTime.now()); // 판매 완료 시간 설정

                LinkInfo linkInfo = linkInfoRepository.findByVhrno(product.getCarPlateNumber())
                        .orElseThrow(() -> new IllegalArgumentException("차량 정보를 찾을 수가 없습니다."));
                linkInfo.setDelYn(YN.Y);
            }

            // 제품 상태가 SOLDOUT로 변경된 경우에만 알림톡 전송
            if (PostStatus.SOLDOUT.equals(product.getPostStatus())) {
                if (requestDto.getPurchaseInquiryId() != null) {
                    PurchaseInquiry purchaseInquiry = purchaseInquiryRepository
                            .findById(requestDto.getPurchaseInquiryId())
                            .orElseThrow(() -> new IllegalArgumentException("문의 내역을 찾을 수가 없습니다."));

                    // 회원인 경우
                    if (purchaseInquiry.getMemberId() != null) {
                        Member member = memberRepository.findById(purchaseInquiry.getMemberId())
                                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수가 없습니다."));

                        String realName = member.getRealName();
                        String mobileNumber = member.getMobileNumber();
                        String carPlateNumber = product.getCarPlateNumber();
                        LocalDateTime soldOutAt = product.getSoldOutAt(); // 판매 완료 시간 가져오기
                        String soldOutFormat = soldOutAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                        Notification notification = Notification.builder()
                                .memberId(member.getId())
                                .target("personal")
                                .pushStatus("send")
                                .sendStatus("right")
                                .title(ConsultationStatus.PAYMENT_COMPLETE.getDetail())
                                .content("구입하신 [" + carPlateNumber +"] 차량의 결제가 완료되었습니다. \n" +
                                        "\n" +
                                        "결제일 : " + soldOutFormat + "\n" +
                                        "차량번호 : " + carPlateNumber + "\n" +
                                        "\n" +
                                        "카통령을 이용해주셔서 감사합니다. \n" +
                                        "구매하신 차량은 카통령에서 평생지원서비스가 제공됩니다.\n")
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

                        Map<String, String> placeholders = new HashMap<>();
                        placeholders.put("#{realName}", realName);
                        placeholders.put("#{carPlateNumber}", carPlateNumber);
                        placeholders.put("#{day}", soldOutFormat); // 판매 완료 시간 사용

                        if (YN.Y.equals(member.getAgreeNoticeYn())) {
                            alimTalkService.sendAlimTalk("code-05", placeholders, mobileNumber);
                        }
                    }
                    // 비회원인 경우
                    else {
                        String realName = purchaseInquiry.getRealName();
                        String mobileNumber = purchaseInquiry.getMobileNumber();
                        String carPlateNumber = product.getCarPlateNumber();
                        LocalDateTime soldOutAt = product.getSoldOutAt(); // 판매 완료 시간 가져오기

                        Map<String, String> placeholders = new HashMap<>();
                        placeholders.put("#{realName}", realName);
                        placeholders.put("#{carPlateNumber}", carPlateNumber);
                        placeholders.put("#{day}", soldOutAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // 판매 완료 시간 사용

                        alimTalkService.sendAlimTalk("code-05", placeholders, mobileNumber);
                    }
                }
            }
        });

    }

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 게시 중지 > 팝업 > 저장
     *
     * @param requestDto 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminManagementProductReasonSave(ProductSaveDto requestDto) {
        Product product = productRepository
                .findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수가 없습니다."));

        product.setPostStatus(PostStatus.POSTSTOP);
        product.setPostStopReason(requestDto.getReason());
    }

    /**
     * 관리자 | 차량 관리 > 판매 차량 > 게시 중지 > 팝업 - 게시 중지 사유 가져오기
     *
     * @param id 상품 시퀀스
     * @author SungHa
     */
    public String findPostStopReasonById(Long id) {
        String reason = null;
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            reason = product.get().getPostStopReason();
        }

        return reason;
    }

    /**
     * 관리자 | 프로모션 > 메인 상품 진열 > 목록, 추천차량 설정 팝업
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public List<ProductListDto> searchAllProduct(ProductSearchDto condition) {
        return productRepository.searchAllProduct(condition);
    }

    /**
     * 관리자 | 프로모션 > 메인 상품 진열 > 추천차량 팝업 > 저장
     * 관리자 | 프로모션 > 메인 상품 진열 > 목록 > 삭제
     *
     * @param requestDto 등록할 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminPromotionRecommendSave(ProductSaveDto requestDto) {
        for (Long id : requestDto.getId()) {
            Product product = productRepository
                    .findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

            if ("car".equals(requestDto.getType())) {
                if (product.getCartongryeongRecommendYn() == YN.N) {
                    product.setCartongryeongRecommendYn(YN.Y);
                } else {
                    product.setCartongryeongRecommendYn(YN.N);
                }
            } else {
                if (product.getBestValueRecommendYn() == YN.N) {
                    product.setBestValueRecommendYn(YN.Y);
                } else {
                    product.setBestValueRecommendYn(YN.N);
                }
            }
        }
    }

    /**
     * 홈 > 월별 주요 현황 > 판매 건수
     *
     * @author SungHa
     */
    public Long searchAllSoldOutProductCountByMain() {
        return productRepository.searchAllSoldOutProductCountByMain();
    }

    /**
     * 홈 > 월별 주요 현황 > 누적 매출
     *
     * @author SungHa
     */
    public Long searchAllSalesByMain() {
        return productRepository.searchAllSalesByMain();
    }

    /**
     * 홈 > 월별 주요 현황 > 등록차량
     *
     * @author SungHa
     */
    public Long searchAllProductCountByMain() {
        return productRepository.searchAllProductCountByMain();
    }

    /**
     * 관리자 | 차량관리 > 판매차량 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author Sangbin
     */
    public Workbook searchAllPostingProductExcelLists(ProductSearchDto condition) {
        // 리스트 추출
        List<ProductListDto> list = productRepository.searchAllPostingProductExcelLists(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("판매 차량");

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
        cell.setCellValue("차량 NO");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("차량 번호");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상사명");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("차량명");
        sheet.setColumnWidth(colNo++, 10000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("연식");
        sheet.setColumnWidth(colNo++, 2000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("주행거리");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("가격");
        sheet.setColumnWidth(colNo++, 5000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("등록일");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("상태");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("딜러명");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("지역");
        sheet.setColumnWidth(colNo++, 4000);

        // 데이터 부분 생성
        int i = 0;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        for (ProductListDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(list.size() - i);
            i++;

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getProductUniqueNumber());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getCarPlateNumber());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getShopName());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getMakerName() + " " + vo.getModelName() + " " + vo.getDetailGradeName());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getCarRegYear());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(numberFormat.format(vo.getCarUseKm()) + "km");

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(numberFormat.format(vo.getCarAmountSale()));

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String createdAt = (vo.getCreatedAt() != null) ? vo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "-";
            cell.setCellValue(createdAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getPostStatus().getDetail());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getRealName());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getCity());

        }

        return wb;
    }

    /**
     * 관리자 | 내 차 구입 > 연락 가능 문의 > 엑셀
     *
     * @param condition 검색 조건을 담은 DTO
     * @author SungHa
     */
    public Workbook searchRecommendProductExcelList(ProductSearchDto condition) {

        condition.setStatus(Collections.singletonList(PostStatus.POST.getCode()));
        condition.setShopName("디에스 오토");
        condition.setType("product");

        // 리스트 추출
        List<ProductListDto> list = productRepository.searchRecommendProductExcelList(condition);

        // 워크북 생성
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("추천 차량");

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
        cell.setCellValue("차량 NO");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("차량 번호");
        sheet.setColumnWidth(colNo++, 10000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("차량명");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("연식");
        sheet.setColumnWidth(colNo++, 5000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("주행거리");
        sheet.setColumnWidth(colNo++, 6000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("가격");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("등록일");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("딜러명");
        sheet.setColumnWidth(colNo++, 4000);

        cell = row.createCell(colNo);
        cell.setCellStyle(headStyle);
        cell.setCellValue("지역");
        sheet.setColumnWidth(colNo++, 4000);

        // 데이터 부분 생성
        int i = 0;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        for (ProductListDto vo : list) {

            colNo = 0;
            row = sheet.createRow(rowNo++);

            cell = row.createCell(colNo++);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(list.size() - i);
            i++;

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getProductUniqueNumber());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getCarPlateNumber());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getMakerName() + " " + vo.getModelName() + " " + vo.getDetailGradeName());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getCarRegYear());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(numberFormat.format(vo.getCarUseKm()) + "km");

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(numberFormat.format(vo.getCarAmountSale()));

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            String createdAt = (vo.getCreatedAt() != null) ? vo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "-";
            cell.setCellValue(createdAt);

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getRealName());

            cell = row.getCell(colNo++, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(vo.getCity());

        }

        return wb;
    }

}
