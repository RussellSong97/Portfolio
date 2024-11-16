package com.cuv.domain.exhibition;

import com.cuv.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.domain.exhibition.dto.ExhibitionSearchDto;

import java.util.List;

public interface ExhibitionRepositoryCustom {
    List<ExhibitionListDto> searchAllExhibition(String exhibitionType);

    List<ExhibitionListDto> searchAllEvent(ExhibitionSearchDto condition);

    ExhibitionDetailDto searchExhibitionById(Long id);

}
