package com.cuv.domain.product.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.product.enumset.PostStatus;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Setter
@Getter
public class ProductDetailDto {

    private Long productId;

    private String productUniqueNumber;

    private String carPlateNumber;

    private Long productDetailGradeNumber;

    @Convert(converter = AttachmentDto.AttachmentDtoListConverter.class)
    private List<AttachmentDto> carImageUrl;

    private String shopName;

    private String makerName;

    private String modelName;

    private String detailGradeName;

    // 찜 수
    private Long pickCount;

    // 찜 상품 아이디
    private Long pickProductId;

    private boolean picked;

    private String detailModelName;

    // 차량 연식
    private String carRegYear;

    // 주행 거리
    private Long carUseKm;

    // 판매 금액
    private Long carAmountSale;

    // 판매 상태 (대기, 게시, 게시 중지, 판매 완료)
    @Convert(converter = PostStatus.PostStatusConverter.class)
    private PostStatus postStatus;

    @Convert(converter =  AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto profileImageJson;

    // 도시 명
    private String city;

    // 추천 차량 yn
    private YN recommendYn;

    // 조회수
    private Integer hits;

    private String carContent;

    private String carFuel;

    private String carColor;

    private String carMission;

    // 딜러 정보
    private String dealerName;

    private String mobileNumber;

    private String intro;

    // 기본정보
    private String gradeFuelRate;

    private String tireSizeFront;

    private String tireSizeBack;

    private String carGradeNm;

    private Long carClassNbr;

    private String enginesize;

    private String istdTrans;

    private String extShape;

    private String person;

    private String engineForm;

    private String prye;

    private String insptValidPdDe;

    private String employeeNumber;

    private String frstRegistDe;

    private LocalDate afterServiceDate;

    private String carClassNm;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto dealerProfileImage;

    // 제원 정보
    private String specImageId;

    private String specNm;

    // linkListing 정보
    private String checkouturl;

    public String getFormattedCarAmountSale() {
        if (carAmountSale != null) {
            return String.valueOf(carAmountSale / 10000);
        } else {
            return "N/A";
        }
    }

    public String getFormattedMobileNumber() {
        if (mobileNumber != null && !mobileNumber.isEmpty()) {
            return formatMobileNumber(mobileNumber);
        } else {
            return "";
        }
    }

    private String formatMobileNumber(String number) {
        // 한국의 전화번호 형식에 맞춰 포맷팅
        Pattern pattern = Pattern.compile("(\\d{3})(\\d{4})(\\d{4})");
        Matcher matcher = pattern.matcher(number);
        if (matcher.matches()) {
            return matcher.replaceAll("$1-$2-$3");
        } else {
            // 다른 형식의 번호는 그냥 반환
            return number;
        }
    }
}
