package com.cuv.domain.product;

import com.cuv.common.JSONResponse;
import com.cuv.domain.linkcode.LinkCodeRepository;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.pick.PickRepository;
import com.cuv.domain.product.dto.*;
import com.cuv.domain.product.entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final PickRepository pickRepository;
    private final LinkCodeRepository linkCodeRepository;

    public Page<ProductRecommendListDto> searchProductRecommendList(PageRequest request, UserDetails userDetails) {
        Page<ProductRecommendListDto> recommendList = productRepository.searchProductRecommendList(request);

        if (userDetails != null) {
            String username = userDetails.getUsername();
            Member member = memberRepository.findByEmail(username);
            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
            recommendList.forEach(dto -> {
                if (pickedProductIds.contains(dto.getProductId())) {
                    dto.setPicked(true);
                }
            });
        }

        return recommendList;
    }

    /**
     * @param id: productId
     */
    public ProductDetailDto searchProductDetail(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        ProductDetailDto productDetail = productRepository.searchProductDetail(id);
        
        // 픽 카운트 업데이트
        productDetail.setPickCount(productRepository.searchPickCount(id));

        if (member != null) {
            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());

                if (pickedProductIds.contains(productDetail.getProductId())) {
                    // 픽 여부 업데이트
                    productDetail.setPicked(true);
                }
            return productDetail;
        }
        return productDetail;
    }

    public List<ProductRecommendListDto> searchProductRecommendNoPageList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if (member != null) {
            List<ProductRecommendListDto> productRecommendList = productRepository.searchProductRecommendNoPageList();

            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
            productRecommendList.forEach(dto -> {
                if (pickedProductIds.contains(dto.getProductId())) {
                    dto.setPicked(true);
                }
            });
            return productRecommendList;
        }
        return productRepository.searchProductRecommendNoPageList();
    }

    public List<SpecGroupDto> searchProductLinkSpecList(Long productDetailGradeNumber) {
        return productRepository.searchProductLinkSpecList(productDetailGradeNumber);
    }

    public List<SpecGroupDto> searchProductLinkOptionList(Long productDetailGradeNumber) {
        return productRepository.searchProductLinkOptionList(productDetailGradeNumber);
    }

    /**
     * 메인 > 판매 차량 개수
     *
     * @author SungHa
     */
    public Long searchProductCount() {
        return productRepository.searchProductCount();
    }

    public List<ProductRecentCarListDto> searchProductRecentCarList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if (member != null) {
            List<ProductRecentCarListDto> productRecentCarList = productRepository.searchProductRecentCarList();

            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
            productRecentCarList.forEach(dto -> {
                if (pickedProductIds.contains(dto.getProductId())) {
                    dto.setPicked(true);
                }
            });
            return productRecentCarList;
        }
        return productRepository.searchProductRecentCarList();
    }

    public List<ProductCuvRecommendListDto> searchProductCuvRecommendList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if (member != null) {
            List<ProductCuvRecommendListDto> productCuvRecommendList = productRepository.searchProductCuvRecommendList();

            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
            productCuvRecommendList.forEach(dto -> {
                if (pickedProductIds.contains(dto.getProductId())) {
                    dto.setPicked(true);
                }
            });
            return productCuvRecommendList;
        }
            return productRepository.searchProductCuvRecommendList();
    }

    public List<ProductBestValueRecommendListDto> searchProductBestValueRecommendList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if(member != null) {
            List<ProductBestValueRecommendListDto> productBestValueRecommendList = productRepository.searchProductBestValueRecommendList();

            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
            productBestValueRecommendList.forEach(dto -> {
                if (pickedProductIds.contains(dto.getProductId())) {
                    dto.setPicked(true);
                }
            });
            return productBestValueRecommendList;
        }
        return productRepository.searchProductBestValueRecommendList();
    }

    public void addHits(Long id) {
        productRepository.findById(id).ifPresent(Product::addHits);
    }

    public List<ProductListDto> searchProductTotalSearchList(ProductTotalSearchDto condition) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByEmail(email);

        if(member != null) {
            List<ProductListDto> productList = productRepository.searchProductTotalSearchList(condition);

            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
            productList.forEach(dto -> {
                if (pickedProductIds.contains(dto.getProductId())) {
                    dto.setPicked(true);
                }
            });
            return productList;
        }
        return productRepository.searchProductTotalSearchList(condition);
    }

    public JSONResponse<?> searchApiProduct(Map<String, Object> map) {
        List<LinkCodeListDto> categoryChildrenList = null;
        List<ProductListDto> productList = null;
        Long categoryId = map.get("categoryId") != null ? Long.parseLong(map.get("categoryId").toString()) : null;
        Integer depth = map.get("depth") != null ? Integer.parseInt(map.get("depth").toString()) : 0;

        try {
            if (depth != null) {
                try {
                    categoryChildrenList = linkCodeRepository.searchApiCategoryChildrenList(categoryId, depth);
                } catch (Exception e) {
                    log.error("error Message searchChildren : " + e.getMessage());
                    return new JSONResponse<>(500, "FAIL", e.getMessage());
                }

            }
            try {
                productList = productRepository.searchApiProduct(map);
            } catch (Exception e) {
                log.error("error Message search Result : " + e.getMessage());
                return new JSONResponse<>(500, "FAIL", e.getMessage());
            }

        } catch (Exception  e) {
            log.error("error Message : " + e.getMessage());
            return new JSONResponse<>(500, "FAIL", e.getMessage());
        }

        return new JSONResponse<>(200, "SUCCESS", productList, categoryChildrenList);

    }
    /*
     * 상품 상세의 픽 카운트 가져오기
     */
    public Long getProductIdOfPick(Long productId) {
        return  pickRepository.searchPickCountByProductId(productId);
    }

    public JSONResponse<?> searchMobileProductCount(Map<String, Object> map) {
        System.out.println("넘어온 map : " + map);
        Long count = productRepository.searchMobileProductCount(map);
        return new JSONResponse<>(200, "SUCCESS", count);
    }

    public Long countById() {
        Long count = productRepository.countByProductId();
        return count;
    }

    public JSONResponse<?> searchCategory(Map<String, Object> map) {
        Long categoryId = Long.valueOf(map.get("categoryId").toString());
        Integer depth = Integer.valueOf(map.get("depth").toString());

        List<LinkCodeListDto> categoryChildrenList = linkCodeRepository.searchApiCategoryChildrenList(categoryId, depth);
        return new JSONResponse<>(200, "SUCCESS", categoryChildrenList);
    }

    public JSONResponse<?> searchMobileKind(Map<String, Object> map) {
        String engName = map.get("engName").toString();

        List<Long> modelIds = productRepository.searchExtShape(engName);
        List<LinkCodeListDto> categoryId2List = linkCodeRepository.searchCategory2ListName(modelIds);
        return new JSONResponse<>(200, "SUCCESS", categoryId2List);
    }
}
