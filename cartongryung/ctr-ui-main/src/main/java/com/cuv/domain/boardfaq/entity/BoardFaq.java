package com.cuv.domain.boardfaq.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "board_faq")
public class BoardFaq extends BaseEntity {

    /** 게시판 FAQ 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_faq_id")
    private Long id;

    /** 제목 */
    private String title;

    /** 내용 */
    private String content;

    /** 노출 여부 */
    private YN viewYn;

}
