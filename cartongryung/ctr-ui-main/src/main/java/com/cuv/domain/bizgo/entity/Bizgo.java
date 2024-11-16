package com.cuv.domain.bizgo.entity;

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
@Table(name = "bizgo")
public class Bizgo extends BaseEntity {
    /**
     * 게시판 FAQ 일련번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bizgo_id")
    private Long id;

    private String code;

    @Lob
    private String content;

    private YN viewYn;
}