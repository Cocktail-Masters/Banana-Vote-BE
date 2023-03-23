package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.vote.controller.dto.CreateVoteRequest;
import com.cocktailmasters.backend.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "vote", description = "투표 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    @Operation(summary = "투표 생성", description = "새로운 투표 생성")
    @PostMapping("")
    public ResponseEntity<String> createVote(Long userId,
                                             CreateVoteRequest createVoteRequest) throws Exception {
        //TODO: 사용자 검사
        if (voteService.createVote(userId, createVoteRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("");
        }
        throw new Exception();
    }
}
