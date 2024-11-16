package com.cuv.web.controller.pick;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.pick.PickService;
import com.cuv.domain.pick.dto.PickListDto;

import com.cuv.domain.pick.entity.Pick;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PickController {
    private final PickService pickService;
    private final MemberRepository memberRepository;

    /**
     * 찜 DB에 insert
     *
     * @param map:productId
     * @author 송다운
     */
    @PostMapping("/api/sub/pick/v2")
    @ResponseBody
    public JSONResponse<?> apiSubPicV2(@RequestBody Map<String, Object> map , @AuthenticationPrincipal UserDetails userPrincipal) {

        System.out.println("넘어온 productId : " + map.get("productId"));
        JSONResponse<?> response;
        try {
            response = pickService.insertPickV2(map, userPrincipal);
        } catch (Exception e) {
            System.err.println("서비스 호출 중 예외 발생: " + e.getMessage());
            return new JSONResponse<>(500, "FAIL");
        }
        return response;
    }

    /**
     * 픽만 가져오기
     *
     * @author 박성민
     */
    @GetMapping("/api/sub/getPick")
    public JSONResponse<?> apiGetPicks() {
        // 가져오기
        List<Pick> picks = pickService.getPicks();

        // picks에 readYn에 전부 Y면 200 보내기
        for (Pick pick : picks) {
            if (!pick.getReadYn().equals(YN.Y)) {
                return new JSONResponse<>(400, "Some picks have not been read yet");
            }
        }

        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * readYN에서 N의 숫자 가져오기
     */

    // count로 가져오기
    @GetMapping("/api/sub/getPickCount")
    public JSONResponse<?> getPickCount(@AuthenticationPrincipal UserDetails userDetails) {

        // 가져오기
        Long readYnCount = pickService.searchPickReadYnCount();

        //readYnCount이 0이면 200 보내기
        if (readYnCount != 0) {
            return new JSONResponse<>(400, "Some picks have not been read yet");
        }

        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     * 선택된 픽 제거
     *
     * @author 박성민
     */
    @PostMapping("/api/sub/delete")
    @ResponseBody
    public JSONResponse<?> deletePick(@RequestBody Map<String, Object> map) {

        List<String> pickIds = (List<String>) map.get("pickIds");

        // 문자열을 Long 타입으로 변환
        List<Long> longPickIds = pickIds.stream()
                .map(Long::parseLong)
                .toList();

        for (Long pickId : longPickIds) {
            pickService.updateDelYn(pickId);
        }

        // 리턴하면서 문제 터짐
        return new JSONResponse<>(200, "SUCCESS");
    }

    /**
     *
     *  메인페이지 > 물건 픽
     * @author 박성민
     */
    @ResponseBody
    @GetMapping("/api/sub/getProductIdOfPick")
    public List<Long> getProductIdOfPick() {

        return pickService.getProductIdOfPick();
    }

    /**
     *
     *  메인페이지 > 물건 픽 취소
     * @author 박성민
     */
    @ResponseBody
    @GetMapping("/api/sub/getDelYNOfPick/{productIdStr}")
    public List<YN> getDelYNOfPick(@PathVariable String productIdStr) {

        Long productId = Long.parseLong(productIdStr);
        return pickService.getDelYNOfPick(productId);
    }

}
