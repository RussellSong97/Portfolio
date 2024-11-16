package com.cuv.domain.bizgo.entity;

import com.cuv.common.BaseEntity;
import com.cuv.common.YN;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 알림톡
 */
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "bizgo")
public class Bizgo extends BaseEntity {

    /** 알림톡 일련번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bizgo_id")
    private Long id;

    /** 템플릿 코드 값 */
    private String code;

    /** 템플릿 */
    private String content;

    /** 노출 여부 */
    private YN viewYn;
}
