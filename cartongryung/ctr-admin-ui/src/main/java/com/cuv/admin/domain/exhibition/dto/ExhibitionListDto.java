package com.cuv.admin.domain.exhibition.dto;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.dto.AttachmentDto;
import com.cuv.admin.domain.exhibition.enumset.ExhibitionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(
        name = "ExhibitionListDto",
        description = "전시 테이블(이벤트, 팝업, 상단 띠 배너, 메인 상단 배너, 메인 서브 배너, 로그인 배너) 목록 출력"
)
public class ExhibitionListDto {

    private Long id;

    @Convert(converter = ExhibitionType.ExhibitionTypeConverter.class)
    private ExhibitionType exhibitionType;

    private String title;

    private String content;

    @Convert(converter = AttachmentDto.AttachmentDtoConverter.class)
    private AttachmentDto pcAttachment;

    private LocalDate exhibitionStartDate;

    private LocalDate exhibitionEndDate;

    private LocalDateTime createdAt;

    private YN viewYn;

    private Long hits;

    /**
     * 관리자 | 프로모션 > 이벤트 관리 > 목록
     *
     * @param area 영역
     * @param sdate 노출 시작 날짜
     * @param edate 노출 종료 날짜
     * @author SungHa
     */
    public String getEventStatus(String area, LocalDate sdate, LocalDate edate) {
        LocalDate now = LocalDate.now();

        if ("class".equals(area)) {
            return (!now.isBefore(sdate) && !now.isAfter(edate)) ? "working" : ""; // 진행 중일 때 class 추가
        } else {
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

}
