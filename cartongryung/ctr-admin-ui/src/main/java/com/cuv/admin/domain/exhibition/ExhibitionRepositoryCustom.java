package com.cuv.admin.domain.exhibition;

import com.cuv.admin.domain.exhibition.dto.ExhibitionDetailDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionListDto;
import com.cuv.admin.domain.exhibition.dto.ExhibitionSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExhibitionRepositoryCustom {
    Long searchAllExhibitionCount(String type);

    Page<ExhibitionListDto> searchAllExhibition(ExhibitionSearchDto condition, Pageable request);

    ExhibitionDetailDto searchExhibitionById(Long id);

}
