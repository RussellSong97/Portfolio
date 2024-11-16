package com.cuv.admin.domain.boardguide;

import com.cuv.admin.domain.boardguide.dto.BoardGuideDetailDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideListDto;
import com.cuv.admin.domain.boardguide.dto.BoardGuideSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardGuideRepositoryCustom {
    Page<BoardGuideListDto> searchAllBoardGuide(BoardGuideSearchDto condition, Pageable pageable);

    BoardGuideDetailDto searchBoardGuideById(Long id);

}
