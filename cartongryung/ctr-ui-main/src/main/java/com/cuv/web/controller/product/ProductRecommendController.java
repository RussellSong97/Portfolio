package com.cuv.web.controller.product;

import com.cuv.domain.pick.PickService;
import com.cuv.domain.product.ProductRepository;
import com.cuv.domain.product.ProductService;
import com.cuv.domain.product.dto.ProductDetailDto;
import com.cuv.domain.product.dto.ProductRecommendListDto;
import com.cuv.domain.product.dto.SpecGroupDto;
import com.cuv.domain.productviewshistory.ProductViewsHistoryService;
import com.cuv.util.KISA_SEED_CBC;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductRecommendController {
    private final ProductService productService;
    private final PickService pickService;
    private final ProductRepository productRepository;
    @Value("${carhistory.joinCode}")
    private String joinCode;
    @Value("${carhistory.sType}")
    private String sType;
    @Value("${carhistory.memberID}")
    private String memberID;
    @Value("${carhistory.carNumType}")
    private String carNumType;
    @Value("${carhistory.seed.key}")
    private String seedKey;
    @Value("${carhistory.rType}")
    private String rType;
    private String page;
    private String size;
    private final ProductViewsHistoryService productViewsHistoryService;

    /**
     * 사용자 > 추천 차량 리스트
     * @param model
     * @Author 송다운
     * @return
     */
    @GetMapping("/product/recommend")
    public String productRecommend(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        int setPage = 1;
        int setSize = 50;

        if(StringUtils.hasText(page) && page.matches("^\\d+$")) setPage = Math.max(Integer.parseInt(page), 1);
        if(StringUtils.hasText(size) && size.matches("^\\d+$")) setSize = Math.max(Integer.parseInt(size), 1);

        PageRequest request = PageRequest.of(setPage - 1, setSize, Sort.by("id").descending());

        Page<ProductRecommendListDto> recommendList = productService.searchProductRecommendList(request, userDetails);

        model.addAttribute("recommendList", recommendList);
        return "vehicle/recommend_list";
    }

    /**
     * 차량 상세
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        ProductDetailDto productDetail = productService.searchProductDetail(id);

        List<ProductRecommendListDto> recommendList = productService.searchProductRecommendNoPageList();
        List<SpecGroupDto> groupedSpecList = productService.searchProductLinkSpecList(productDetail.getProductDetailGradeNumber());
        List<SpecGroupDto> linkOptionList = productService.searchProductLinkOptionList(productDetail.getProductDetailGradeNumber());


        // 상품 상세페이지 진입시 최근본차량 insert
        // 비로그인 상태에서 최근 본 상품 추가
        if (userDetails == null) {
            productViewsHistoryService.addRecentCarToSession(id);
        } else {
            // 로그인 상태에서 최근 본 차량 추가
            productViewsHistoryService.saveProductViewsHistory(id, userDetails);
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        byte bszUser_key[] = {
        };
        byte bszIV[] = {
                (byte)0x026, (byte)0x08d, (byte)0x066, (byte)0x0a7,
                (byte)0x035, (byte)0x0a8, (byte)0x01a, (byte)0x081,
                (byte)0x06f, (byte)0x0ba, (byte)0x0d9, (byte)0x0fa,
                (byte)0x036, (byte)0x016, (byte)0x025, (byte)0x001
        };

        String stdDate = today.format(formatter);
        //TODO 테스트를 위한 테스트 차량 번호 실서버는 추후 주석 제거
        String carnum = productDetail.getCarPlateNumber();
//        String carnum = "08오5060";
        String enc_sType = null, enc_carnum = null, enc_memberID = null;
        // 받은 암호화 키
        bszUser_key = KISA_SEED_CBC.hexToByteArray(seedKey);

        try {
            enc_sType = KISA_SEED_CBC.SeedEncryptPlaintext(sType, bszUser_key, bszIV);
            enc_carnum = KISA_SEED_CBC.SeedEncryptPlaintext(carnum, bszUser_key, bszIV);
            enc_memberID = KISA_SEED_CBC.SeedEncryptPlaintext(memberID, bszUser_key, bszIV);
        } catch(Exception e) {
            e.printStackTrace();
        }
        productService.addHits(id);
        String regYear = productDetail.getCarRegYear();
        int carRegYear = Integer.parseInt(regYear); // String을 int로 변환
        int currentYear = Year.now().getValue(); // 현재 년도 구하기
        int yearDifference = currentYear - carRegYear; // 현재 년도에서 carRegYear 빼기

        model.addAttribute("joinCode", joinCode);
        model.addAttribute("sType", enc_sType);
        model.addAttribute("memberID", enc_memberID);
        model.addAttribute("carnum", enc_carnum);
        model.addAttribute("carNumType", carNumType);
        model.addAttribute("stdDate", stdDate);
        model.addAttribute("rType", rType);


        model.addAttribute("productDetail", productDetail);
        model.addAttribute("recommendList", recommendList);
        model.addAttribute("groupedSpecList", groupedSpecList);
        model.addAttribute("linkOptionList", linkOptionList);
        model.addAttribute("yearDifference", yearDifference);
        return "vehicle/vehicle_view";
    }

    /**
     * 차량상세 > 픽 카운트
     * @Author 박성민
     */
    @ResponseBody
    @GetMapping("/api/product/getPickCountOfProduct/{id}")
    public Long getPickCountOfProduct(@PathVariable Long id) {
        return productService.getProductIdOfPick(id);
    }
}
