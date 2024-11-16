package com.cuv.admin.domain.popularkeyword.entity;

import com.cuv.admin.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 인기 검색어
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "popular_keyword")
public class PopularKeyword extends BaseEntity {

    /** 인기 검색어 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "popular_keyword_id")
    private Long id;

    /** 제조사 일련번호 */
    private Long makerCodeId;

    /** 모델 일련번호 */
    private Long modelCodeId;

}
