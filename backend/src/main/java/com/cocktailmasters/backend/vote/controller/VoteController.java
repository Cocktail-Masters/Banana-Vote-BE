package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.vote.controller.dto.CreateVoteRequest;
import com.cocktailmasters.backend.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    @PostMapping("")
    public ResponseEntity<String> createVote(Long userId,
                                             CreateVoteRequest createVoteRequest) throws Exception {
        //TODO: 사용자 검사 및 입력 값 검사
        if (voteService.createVote(userId, createVoteRequest)) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("");
        }
        //TODO: 예외 특정
        throw new Exception();
    }
}
