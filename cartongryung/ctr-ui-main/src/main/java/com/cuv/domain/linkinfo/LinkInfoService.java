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

    public JSONResponse<?> searchApiProductIstdTrans() {

        List<LinkInfoListDto> list = linkInfoRepository.searchApiProductIstdTrans();
        return new JSONResponse<>(200, "SUCCESS", list);
    }

    public JSONResponse<?> searchApiProductColor() {
        List<LinkInfoListDto> colorList = linkInfoRepository.searchApiProductColor();
        return new JSONResponse<>(200, "SUCCESS", colorList);
    }
}
