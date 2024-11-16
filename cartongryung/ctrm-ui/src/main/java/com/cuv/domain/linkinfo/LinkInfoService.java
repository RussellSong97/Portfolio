package com.cuv.domain.linkinfo;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkinfo.dto.LinkInfoListDto;
import com.cuv.domain.linkinfo.entity.LinkInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkInfoService {
    private final LinkInfoRepository linkInfoRepository;

    /**
     * 검색 페이지 색상 데이터
     * @return
     */
    public JSONResponse<?> searchApiProductColor() {
        List<LinkInfoListDto> colorList = linkInfoRepository.searchApiProductColor();
        return new JSONResponse<>(200, "SUCCESS", colorList);
    }

    /**
     * 검색 색상 더 보기 데이터
     * @return
     */
    public JSONResponse<?> searchApiProductMoreColor() {
        List<LinkInfoListDto> colorMoreList = linkInfoRepository.searchApiProductMoreColor();
        return new JSONResponse<>(200, "SUCCESS",colorMoreList);
    }
}
