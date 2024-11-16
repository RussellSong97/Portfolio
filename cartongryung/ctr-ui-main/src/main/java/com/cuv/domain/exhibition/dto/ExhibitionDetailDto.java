package com.cuv.domain.exhibition.dto;

import com.cuv.common.YN;
import com.cuv.domain.attachment.dto.AttachmentDto;
import com.cuv.domain.exhibition.enumset.BackgroundColorType;
import com.cuv.domain.exhibition.enumset.ExhibitionType;
import com.cuv.domain.exhibition.enumset.LinkType;
import jakarta.persistence.Convert;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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
