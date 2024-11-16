package com.cuv.web.controller.pick;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.pick.PickService;
import com.cuv.domain.pick.entity.Pick;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
        name = "사용자 - 찜",
        description = "사용자 - 찜"
)
@RequiredArgsConstructor
@Controller
@Slf4j
public class PickController {
    private final PickService pickService;

    /**
     * 찜 저장
     * @param map:productId
     * @author 송다운
     */
    @Operation(
            summary = "사용자 - 찜",
            description = "찜 저장"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @PostMapping("/api/sub/pick/v2")
    @ResponseBody
    public JSONResponse<?> apiSubPicV2(@RequestBody Map<String, Object> map , Authentication authentication) {
        JSONResponse<?> response;
        try {
            response = pickService.insertPickV2(map, authentication);
        } catch (Exception e) {
            return new JSONResponse<>(500, e.getMessage());
        }
        return response;
    }

    /**
     * 픽만 가져오기
     * @author 박성민
     */
    @Operation(
            summary = "사용자 - 찜",
            description = "픽만 가져오기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

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
    @Operation(
            summary = "사용자 - 찜",
            description = "readYN에서 N의 숫자 가져오기"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Some picks have not been read yet",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @GetMapping("/api/sub/getPickCount")
    public JSONResponse<?> getPickCount() {
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
    @Operation(
            summary = "사용자 - 찜",
            description = "선택된 픽 제거"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })
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
     *  픽한 상품 정보
     * @author 박성민
     */
    @Operation(
            summary = "사용자 - 찜",
            description = "픽한 상품 정보"
    )
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
    @Operation(
            summary = "사용자 - 찜",
            description = "메인페이지 > 물건 픽 취소"
    )
    @ResponseBody
    @GetMapping("/api/sub/getDelYNOfPick/{productIdStr}")
    public List<Long> getDelYNOfPick(@PathVariable String productIdStr) {

        Long productId = Long.parseLong(productIdStr);
        return pickService.getDelYNOfPick(productId);
    }

    /**
     * 찜 목록 조회
     * @return
     */
    @Operation(
            summary = "사용자 - 찜",
            description = "찜 목록 조회"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status Ok"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = JSONResponse.class)
                    )
            )
    })

    @GetMapping("/api/pick/summary")
    @ResponseBody
    public JSONResponse<?> apiPickSummary() {
        return new JSONResponse<>(200, "SUCCESS", pickService.searchPickSummary());
    }

}
