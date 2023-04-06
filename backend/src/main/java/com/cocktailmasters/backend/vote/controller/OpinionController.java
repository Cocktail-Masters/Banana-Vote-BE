package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateOpinionRequest;
import com.cocktailmasters.backend.vote.controller.dto.opinion.FindOpinionsResponse;
import com.cocktailmasters.backend.vote.service.OpinionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "opinion", description = "의견 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/opinions")
public class OpinionController {

    private final OpinionService opinionService;

    @Operation(summary = "의견 작성", description = "투표 글에 의견 작성")
    @PostMapping()
    public ResponseEntity<String> createOpinion(@Valid @RequestBody CreateOpinionRequest createOpinionRequest) throws Exception {
        // TODO: 토큰으로 사용자 검증
        if (createOpinionRequest.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Long userId = 1L;
        if (opinionService.createOpinion(userId, createOpinionRequest)) {
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
        PageRequest page = PageRequest.of(pageIndex, 10);
        return ResponseEntity.ok()
                .body(opinionService.findOpinions(voteId, sortBy, page));
    }

}
