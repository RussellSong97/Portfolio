package com.cuv.domain.exhibition.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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

    private String title;

    private String content;

    private LocalDate exhibitionStartDate;

    private LocalDate exhibitionEndDate;

    private Long hits;

    /**
     * 이벤트 > 상세
     *
     * @param edate 노출 종료 날짜
     * @author SungHa
     */
    public String getEventStatusClass(LocalDate edate) {
        LocalDate now = LocalDate.now();

        if (now.isAfter(edate)) {
            return "is-end";
        } else {
            return "is-ing";
        }
    }

    /**
     * 이벤트 > 상세
     *
     * @param edate 노출 종료 날짜
     * @author SungHa
     */
    public String getEventStatusText(LocalDate edate) {
        LocalDate now = LocalDate.now();

        if (now.isAfter(edate)) {
            return "종료";
        } else {
            return "진행중";
        }
    }

}
