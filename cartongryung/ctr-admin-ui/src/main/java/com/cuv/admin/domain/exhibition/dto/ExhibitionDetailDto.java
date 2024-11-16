package com.cuv.admin.domain.exhibition.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.exhibition.enumset.BackgroundColorType;
import com.cuv.admin.domain.exhibition.enumset.ExhibitionType;
import com.cuv.admin.domain.exhibition.enumset.LinkType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(
        name = "ExhibitionDetailDto",
        description = "전시 테이블(이벤트, 팝업, 상단 띠 배너, 메인 상단 배너, 메인 서브 배너, 로그인 배너) 상세 출력"
)
public class ExhibitionDetailDto {

    private Long id;

    @Convert(converter = ExhibitionType.ExhibitionTypeConverter.class)
    private ExhibitionType exhibitionType;

    private String title;

    private String content;

    private String linkUrl;

    @Convert(converter = LinkType.LinkTypeConverter.class)
    private LinkType linkType;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto pcAttachment;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto mobileAttachment;

    private LocalDate exhibitionStartDate;

    private LocalDate exhibitionEndDate;

    @Convert(converter = BackgroundColorType.BackgroundColorTypeConverter.class)
    private BackgroundColorType backgroundColorType;

    private YN viewYn;

    private int position;

    /**
     * 관리자 | 프로모션 > 이벤트 관리 > 상세
     *
     * @param sdate 노출 시작 날짜
     * @param edate 노출 종료 날짜
     * @author SungHa
     */
    public String getEventStatus(LocalDate sdate, LocalDate edate) {
        LocalDate now = LocalDate.now();

        // 날짜에 따른 상태값
        if (now.isBefore(sdate)) {
            return "대기";
        } else if (now.isAfter(edate)) {
            return "종료";
        } else {
            return "진행중";
        }
    }

}
