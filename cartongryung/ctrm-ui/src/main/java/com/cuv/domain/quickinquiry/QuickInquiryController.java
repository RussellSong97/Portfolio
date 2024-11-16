package com.cuv.domain.quickinquiry;

import com.cuv.common.JSONResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 간편 상담 신청 컨트롤러
 *
 * @since 2024-09-12
 * @author Joo ree Song
 * @version 1.1
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class QuickInquiryController {

    private final QuickInquiryService quickInquiryService;

    @PostMapping("/api/search/quick/inquiry/sendinfo")
    @ResponseBody
    public JSONResponse<?> uploadFilesInfo(@RequestParam(value="costumerName") String costumerName,
                                           @RequestParam(value="costumerNumber") String costumerNumber,
                                           @RequestParam(value="costumerAsk") String costumerAsk,
                                           @RequestParam(value="entryType") String entryType) {
        try {
            quickInquiryService.applyQuickInquiry(costumerName, costumerNumber, costumerAsk, entryType);

            return new JSONResponse<>(200, "SUCCESS", true);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage(), false);
        }
    }
    /*
    @PostMapping("/quick/inquiry/sendInfo")
    public JSONResponse<?> uploadFilesInfo(@RequestParam(value="costumerName") String costumerName,
                                           @RequestParam(value="costumerNumber") String costumerNumber,
                                           @RequestParam(value="costumerAsk") String costumerAsk,
                                           @RequestParam(value="entryType") String entryType) {
        try {
            quickInquiryService.applyQuickInquiry(costumerName, costumerNumber, costumerAsk, entryType);

            return new JSONResponse<>(200, "SUCCESS", true);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage(), false);
        }
    }*/

}
