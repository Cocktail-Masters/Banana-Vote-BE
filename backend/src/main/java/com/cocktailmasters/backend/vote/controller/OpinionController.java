package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateOpinionRequest;
import com.cocktailmasters.backend.vote.service.OpinionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "opinion", description = "의견 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/opinions")
public class OpinionController {

    private final OpinionService opinionService;

    @Operation(summary = "의견 작성", description = "투표 글에 의견 작성")
    @PostMapping()
    public ResponseEntity<String> createOpinion(@RequestBody CreateOpinionRequest createOpinionRequest) throws Exception {
        // TODO: 토큰으로 사용자 검증
        Long userId = 1L;
        if (opinionService.createOpinion(userId, createOpinionRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }
}
