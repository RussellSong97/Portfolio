package com.cuv.admin.domain.terms;

import com.cuv.admin.domain.terms.dto.TermsSaveDto;
import com.cuv.admin.domain.terms.entity.Terms;
import com.cuv.admin.domain.terms.enumset.TermsType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasText;

/**
 * 서비스 약관 관리 (등록, 수정, 상세, 작성 양식)
 */
@Service
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     * 관리자 | 기타 > 서비스 약관 관리 > 작성 폼 및 상세
     *
     * @param requestDto 글 정보를 담은 DTO
     * @author SungHa
     */
    public TermsSaveDto searchAllTerms(TermsSaveDto requestDto) {
        return termsRepository.searchAllTerms(requestDto);
    }

    /**
     * 관리자 | 기타 > 서비스 약관 관리 > 등록 및 수정
     *
     * @param requestDto 등록할 글 정보를 담은 DTO
     * @author SungHa
     */
    @Transactional
    public void adminEtcTermsWriteProc(TermsSaveDto requestDto) {
        if (!hasText(requestDto.getContent()))
            throw new IllegalArgumentException("내용을 입력해주세요.");

        Terms terms;
        if (requestDto.getId() != null) {
            terms = termsRepository
                    .findById(requestDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("글을 찾을 수 없습니다."));

            terms.setContent(requestDto.getContent());
        } else {
            terms = Terms.builder()
                    .termsType(TermsType.ofCode(requestDto.getType()))
                    .content(requestDto.getContent())
                    .build();

            termsRepository.save(terms);
        }
    }

}
