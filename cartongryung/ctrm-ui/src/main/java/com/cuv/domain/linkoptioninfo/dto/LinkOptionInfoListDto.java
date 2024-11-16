package com.cuv.domain.linkoptioninfo.dto;

import com.cuv.common.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Schema(
        name = "LinkOptionInfoListDto",
        description = "상품 옵션 정보 출력"
)
public class LinkOptionInfoListDto {
    private Long id;
    private String optionName;
    private YN optPickType;
    private Long count;
    private Long carGradeNbr;
    private String optCtgry;
    private String optionNm;
    private Map<String, List<String>> optionList;


}
