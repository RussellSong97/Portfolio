package com.cuv.admin.domain.boardreview;

import com.cuv.admin.common.YN;
import com.cuv.admin.domain.attachment.AttachmentService;
import com.cuv.admin.domain.boardreview.entity.BoardReview;
import com.cuv.admin.domain.memberadmin.MemberAdminRepository;
import com.cuv.admin.domain.memberadmin.entity.MemberAdmin;
import com.cuv.admin.domain.boardreview.dto.BoardReviewDetailDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewListDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewSaveDto;
import com.cuv.admin.domain.boardreview.dto.BoardReviewSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasText;

/**
 * 이용후기 서비스 (조회, 저장, 수정, 삭제)
 */
@Service
@Transactional
@RequiredArgsConstructor
public class BoardReviewService {

    private final BoardReviewRepository reviewRepository;
    private final MemberAdminRepository memberAdminRepository;
    private final AttachmentService attachmentService;

    /**
     * 고객 후기 목록 불러오기
     *
     * @param request 검색 조건을 담은 DTO
     * @author 박성민
     */
    public Page<BoardReviewListDto> searchAllReviewList(BoardReviewSearchDto condition, Pageable request) {
        return reviewRepository.searchAllReviewList(condition, request);
    }

    /**
     * 고객 후기 디테일보기
     *
     * @param reviewId 고객 후기 시퀀스
     * @author 박성민
     */

    public BoardReviewDetailDto searchReviewDetailByReviewId(Long reviewId) {
        return reviewRepository.searchReviewDetailByReviewId(reviewId);
    }

    /**
     * 고객 후기 저장하기
     *
     * @author 박성민
     */

    public void adminReviewWriteProc(BoardReviewSaveDto requestDto) {
        if(!hasText(requestDto.getTitle()))
            throw new IllegalArgumentException("제목을 넣어주세요");
        if(!hasText(requestDto.getViewYn()))
            throw new IllegalArgumentException("노출 여부를 정해주세요");
        if(!hasText(requestDto.getContent()))
            throw new IllegalArgumentException("내용을 넣어주세요");
        if (!hasText(requestDto.getFileUUID()))
            throw new IllegalArgumentException("이미지를 첨부해주세요.");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String memberAdminId = auth.getName();
        MemberAdmin memberAdmin = memberAdminRepository
                .findByLoginId(memberAdminId)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));

        BoardReview boardReview = BoardReview.builder()
                .productId(requestDto.getProductId())
                .memberAdminId(memberAdmin.getId())
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .attachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID())) // 여기서 데이터를 못 받아옴
                .viewYn(YN.ofYn(requestDto.getViewYn()))
                .build();

        try {
            reviewRepository.save(boardReview);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }


    /**
     * 고객 후기 업데이트하기
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author 박성민
     */
    public void adminReviewEditProc(BoardReviewSaveDto requestDto) {
        // 리뷰 불러오기
        BoardReview boardReview = reviewRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // validation
        if(!hasText(requestDto.getTitle()))
            throw new IllegalArgumentException("제목을 넣어주세요");
        if(!hasText(requestDto.getViewYn()))
            throw new IllegalArgumentException("노출 여부를 정해주세요");
        if(!hasText(requestDto.getContent()))
            throw new IllegalArgumentException("내용을 넣어주세요");
        if(!hasText(requestDto.getFileUUID()))
            throw new IllegalArgumentException("이미지를 넣어주세요");

        // 업데이트
        boardReview.setTitle(requestDto.getTitle());
        boardReview.setContent(requestDto.getContent());
        boardReview.setViewYn(YN.ofYn(requestDto.getViewYn()));
        boardReview.setProductId(requestDto.getProductId());
        boardReview.setAttachment(attachmentService.findUploadFileDtoByUUID(requestDto.getFileUUID()));

        // 적용
        try {
            reviewRepository.save(boardReview);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 고객 후기 삭제하기
     *
     * @param id 삭제할 글 시퀀스
     * @author 박성민
     */

    public void adminReviewDeleteProc(Long id) {
       BoardReview boardReview = reviewRepository.findById(id).orElse(null);

       boardReview.setDelYn(YN.Y);

        try {
            reviewRepository.save(boardReview);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
