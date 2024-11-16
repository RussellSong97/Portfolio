package com.cuv.domain.product;

import com.cuv.common.JSONResponse;
import com.cuv.common.YN;
import com.cuv.domain.linkcode.LinkCodeRepository;
import com.cuv.domain.linkcode.dto.LinkCodeListDto;
import com.cuv.domain.member.MemberRepository;
import com.cuv.domain.member.entity.Member;
import com.cuv.domain.member.enumset.MemberStatus;
import com.cuv.domain.pick.PickRepository;
import com.cuv.domain.product.dto.*;
import com.cuv.domain.product.entity.Product;
import com.cuv.domain.productviewshistory.ProductViewsHistoryRepository;
import com.cuv.domain.productviewshistory.entity.ProductViewsHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 차량 조회, 수정 삭제 (차량 검색 조회)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductViewsHistoryRepository productViewsHistoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final PickRepository pickRepository;
    private final LinkCodeRepository linkCodeRepository;


    /**
     * 사용자 - 추천차량 리스트
     * @param request 페이지
     * @param authentication 로그인 정보
     * @return
     */
    public Page<ProductRecommendListDto> searchProductRecommendList(PageRequest request, Authentication authentication) {
        Page<ProductRecommendListDto> recommendList = productRepository.searchProductRecommendList(request);

        if (authentication != null) {
            String username = authentication.getName();
            Member member = memberRepository.findByLoginIdAndStatusCode(username,MemberStatus.NORMAL);
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
     * 사용자 - 상품 상세
     * @param id: productId
     * @return
     */
    public ProductDetailDto searchProductDetail(Long id, UserDetails userDetails) {
        ProductDetailDto productDetail = productRepository.searchProductDetail(id);

        // 픽 카운트 업데이트
        productDetail.setPickCount(productRepository.searchPickCount(id));

        if (userDetails != null) {
            String email = userDetails.getUsername();
            Member member = memberRepository.findByLoginIdAndStatusCode(email, MemberStatus.NORMAL);

            if (member != null) {
                List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
                if (pickedProductIds.contains(productDetail.getProductId())) {
                    // 픽 여부 업데이트
                    productDetail.setPicked(true);
                }
            }
        } else {
            productDetail.setPicked(false);
        }

        return productDetail;
    }


    /**
     * 페이징 없는 추천차량 리스트
     * @return
     */
    public List<ProductRecommendListDto> searchProductRecommendNoPageList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email,MemberStatus.NORMAL);

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


    /**
     * 차량 상세 - 옵션 정보
     * @param productDetailGradeNumber 옵션 정보 조회를 위한 특정 아이디
     * @return
     */
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

    /**
     * 실시간 인기 차량
     * @return
     */
    public List<ProductHitCarListDto> searchProductHitCarList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email,MemberStatus.NORMAL);

        if (member != null) {
            List<ProductHitCarListDto> productHitCarList = productRepository.searchProductHitCarList();

            List<Long> pickedProductIds = pickRepository.findPickedProductIdsByMemberId(member.getId());
            productHitCarList.forEach(dto -> {
                if (pickedProductIds.contains(dto.getProductId())) {
                    dto.setPicked(true);
                }
            });
            return productHitCarList;
        }
        return productRepository.searchProductHitCarList();
    }

    /**
     * 올라온지 얼마 안된 차량 리스트 (메인)
     * @return
     */
    public List<ProductRecentCarListDto> searchProductRecentCarList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email,MemberStatus.NORMAL);

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

    /**
     * 카통령 추천 차량 리스트 (메인)
     * @return
     */
    public List<ProductCuvRecommendListDto> searchProductCuvRecommendList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email,MemberStatus.NORMAL);

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

    /**
     * 가성비 추천 차량 리스트 (메인)
     * @return
     */
    public List<ProductBestValueRecommendListDto> searchProductBestValueRecommendList() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Member member = memberRepository.findByLoginIdAndStatusCode(email,MemberStatus.NORMAL);

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

    /**
     * 차량 상세 - 조회수 증가
     * @param id 차량 아이디
     */
    @Transactional
    public void addHits(Long id) {
        productRepository.findById(id).ifPresent(Product::addHits);
    }


    /**
     * 검색 조건에 관련된 결과
     * @param map 검색 조건
     * @return
     */
    public JSONResponse<?> searchApiProduct(Map<String, String> map) {
        Long categoryId = null;

        try {
            // categoryId 값을 안전하게 파싱
            if (map.containsKey("categoryId") && !map.get("categoryId").trim().isEmpty()) {
                categoryId = Long.valueOf(map.get("categoryId"));
            }

            // productList 및 productCount 조회
            try {
                Page<ProductListDto> productList = productRepository.searchApiProduct(map);

                return new JSONResponse<>(200, "SUCCESS", productList);
            } catch (Exception e) {
                log.error("Error Message search Result : " + e.getMessage(), e);
                return new JSONResponse<>(500, "FAIL", e.getMessage());
            }

        } catch (Exception e) {
            log.error("Error Message : " + e.getMessage(), e);
            return new JSONResponse<>(500, "FAIL", e.getMessage());
        }

    }


    /**
     * 추천 차량 > 스크롤 이벤트
     *
     * @param map 페이지 변수가 담긴 map
     * @author SungHa
     */
    public JSONResponse<?> searchApiRecommendProduct(Map<String, Object> map) {
        List<ProductRecommendListDto> productList = null;

        try {
            try {
                productList = productRepository.searchApiRecommendProduct(map);
            } catch (Exception e) {
                log.error("error Message search Result : " + e.getMessage());
                return new JSONResponse<>(500, "FAIL", e.getMessage());
            }

        } catch (Exception  e) {
            log.error("error Message : " + e.getMessage());
            return new JSONResponse<>(500, "FAIL", e.getMessage());
        }

        return new JSONResponse<>(200, "SUCCESS", productList);

    }


    /**
     * 상품 상세의 픽 카운트 가져오기
     * @param productId 상품 아이디
     * @return
     */
    public Long getProductIdOfPick(Long productId) {
        return  pickRepository.searchPickCountByProductId(productId);
    }

    /**
     * 제조사 카테고리 조회
     * @param map
     * @return
     */
    public JSONResponse<?> searchCategory(Map<String, Object> map) {
        Long categoryId = map.get("categoryId") != null ? Long.valueOf(map.get("categoryId").toString()) : 0L;
        Integer depth = Integer.valueOf(map.get("depth").toString());

        List<LinkCodeListDto> categoryChildrenList = linkCodeRepository.searchApiCategoryChildrenList(categoryId, depth);
        return new JSONResponse<>(200, "SUCCESS", categoryChildrenList);
    }

    /**
     * 모바일 차종 검색
     * @param map 모바일 차종 검색 정보
     * @return
     */
    public JSONResponse<?> searchMobileKind(Map<String, Object> map) {
        String engName = map.get("engName").toString();
        String type = map.get("type").toString();
        List<Long> modelIds = new ArrayList<>();

        if("shape".equals(type)) {
            modelIds = productRepository.searchExtShape(engName);
        } else {
            modelIds = productRepository.searchCarSize(engName);
        }

        List<LinkCodeListDto> categoryId2List = linkCodeRepository.searchCategory2ListName(modelIds);
        return new JSONResponse<>(200, "SUCCESS", categoryId2List);
    }

    /**
     * 최근 본 차량 삭제
     * @param map 최근 본 차량 정보
     * @return
     */
    public JSONResponse<?> deleteRecentProduct(Map<String, Object> map) {
        Long recentId = Long.valueOf(map.get("id").toString());
        ProductViewsHistory productViewsHistory = productViewsHistoryRepository.findById(recentId).orElseThrow(()
                -> new IllegalArgumentException("해당 회원이 없습니다. id=" + recentId));

        productViewsHistory.setDelYn(YN.Y);

        try {
            productViewsHistoryRepository.save(productViewsHistory);
        } catch (Exception e) {
            return new JSONResponse<>(500, "삭제 실패");
        }
        return new JSONResponse<>(200, "SUCCESS");
    }


    /**
     * 상품 상세 - 제원 정보 팝업
     * @param map 제원 정보
     * @return
     */
    public JSONResponse<?> searchApiProductSpecList(Map<String, Object> map) {
        Long productDetailGradeNumber = Long.valueOf(map.get("productDetailGradeNumber").toString());
        List<SpecGroupDto> list =  productRepository.searchProductLinkSpecList(productDetailGradeNumber);
        return new JSONResponse<>(200, "SUCCESS", list);
    }
}
