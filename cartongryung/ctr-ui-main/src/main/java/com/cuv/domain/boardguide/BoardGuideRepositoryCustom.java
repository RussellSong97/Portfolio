package com.cuv.domain.boardguide;

import com.cuv.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.domain.boardguide.dto.BoardGuideListDto;

import java.util.List;

public interface BoardGuideRepositoryCustom {
    List<BoardGuideListDto> searchAllBoardGuide();

    BoardGuideDetailDto searchBoardGuideById(Long id);

}
