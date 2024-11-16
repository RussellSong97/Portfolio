package com.cuv.domain.boardnotice;

import com.cuv.domain.boardnotice.dto.BoardNoticeDetailDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeListDto;
import com.cuv.domain.boardnotice.dto.BoardNoticeSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardNoticeRepositoryCustom {
    Page<BoardNoticeListDto> searchAllBoardNotice(BoardNoticeSearchDto condition, Pageable request);

    BoardNoticeDetailDto searchBoardNoticeById(Long id);

    BoardNoticeDetailDto searchBoardNoticeByMain();
}
