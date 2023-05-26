package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateAgreementRequest;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateOpinionRequest;
import com.cocktailmasters.backend.vote.controller.dto.opinion.FindOpinionNumberResponse;
import com.cocktailmasters.backend.vote.controller.dto.opinion.FindOpinionsResponse;
import com.cocktailmasters.backend.vote.service.OpinionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "opinion", description = "의견 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/opinions")
public class OpinionController {

    private final JwtService jwtService;
    private final OpinionService opinionService;

    @Operation(summary = "의견 작성", description = "투표 글에 의견 작성",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping()
    public ResponseEntity<String> createOpinion(@RequestHeader(name = "Authorization", required = false) String token,
                                                @Valid @RequestBody CreateOpinionRequest createOpinionRequest) throws Exception {
        User user = jwtService.findUserByToken(token);
        if (createOpinionRequest.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (opinionService.createOpinion(user, createOpinionRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "의견 목록 보기",
            description = "의견 목록을 페이지당 10개씩 반환, 정렬은 1: agree, 2:recent")
    @GetMapping("/{vote_id}/{page_index}")
    public ResponseEntity<FindOpinionsResponse> findOpinions(@PathVariable("vote_id") Long voteId,
                                                             @PathVariable("page_index") int pageIndex,
                                                             @RequestParam(value = "sort-by", defaultValue = "1") int sortBy) {
        // TODO: 사용자 검사 필요, 로그인 안 한 상태도 고려
        Long userId = 1L;
        PageRequest page = PageRequest.of(pageIndex, 10);
        return ResponseEntity.ok()
                .body(opinionService.findOpinions(userId, voteId, sortBy, page));
    }

    @Operation(summary = "게시글 의견 개수 보기", description = "게시글의 의견 개수 보기")
    @GetMapping("/{vote_id}/count")
    public ResponseEntity<FindOpinionNumberResponse> findOpinionNumber(@PathVariable("vote_id") Long voteId) {
        return ResponseEntity.ok()
                .body(opinionService.findOpinionNumber(voteId));
    }

    @Operation(summary = "의견 삭제", description = "사용자가 작성한 의견 삭제")
    @DeleteMapping("/{opinion_id}")
    public ResponseEntity<String> deleteOpinion(@PathVariable("opinion_id") Long opinionId) {
        // TODO: 사용자 검사 필요
        Long userId = 1L;
        if (opinionService.deleteOpinion(opinionId, userId)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "의견 추천 or 비추천", description = "의견 추천 or 비추천")
    @PatchMapping("/{opinion_id}")
    public ResponseEntity<String> createAgreement(@PathVariable("opinion_id") Long opinionId,
                                                  @Valid @RequestBody CreateAgreementRequest createAgreementRequest) {
        // TODO: 사용자 검사 필요
        Long userId = 1L;
        if (opinionService.createAgreement(opinionId, userId, createAgreementRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
