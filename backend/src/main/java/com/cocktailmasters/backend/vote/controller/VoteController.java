package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.vote.controller.dto.*;
import com.cocktailmasters.backend.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        //TODO: 사용자 검사 및 예외처리
        if (voteService.createVote(userId, createVoteRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "투표 검색", description = "검색어를 사용하여 투표 검색," +
            "검색 옵션은 정렬기준, 종료유무, 페이지 인덱스")
    @GetMapping("/{page_index}/options")
    public ResponseEntity<FindVotesResponse> findVotes(@PathVariable("page_index") int pageIndex,
                                                       @RequestParam("keyword") String keyword,
                                                       @RequestParam(value = "is-tag", defaultValue = "false") boolean isTag,
                                                       @RequestParam(value = "is-closed", defaultValue = "false") boolean isClosed,
                                                       @RequestParam(value = "order-by", defaultValue = "1") int orderBy) {
        PageRequest page = PageRequest.of(pageIndex, 10);
        return ResponseEntity.ok()
                .body(voteService.findVotes(keyword, isTag, isClosed, orderBy, page));
    }

    @Operation(summary = "투표글 상세 보기", description = "투표글 상세 보기, 투표 조회수 증가")
    @GetMapping("/{vote_id}")
    public ResponseEntity<FindVoteDetailResponse> findVoteDetail(Long userId,
                                                                 @PathVariable("vote_id") Long voteId) {
        //TODO: 사용자 검사 및 예외처리
        return ResponseEntity.ok()
                .body(voteService.findVoteDetail(voteId));
    }

    @Operation(summary = "투표글 댓글 보기", description = "투표글 댓글 보기")
    @GetMapping("/{vote_id}/opinions")
    public ResponseEntity<FindVoteOpinionsResponse> findVoteOpinions(Long userId,
                                                                     @PathVariable("vote_id") Long voteId) {
        //TODO: 사용자 검사 및 예외처리
        return ResponseEntity.ok()
                .body(voteService.findVoteOpinions(voteId));
    }

    @Operation(summary = "투표 유무 확인", description = "투표 유무 확인")
    @GetMapping("/check/{vote_id}")
    public ResponseEntity<FindVoteParticipationResponse> findVoteParticipation(Long userId,
                                                                               @PathVariable("vote_id") Long voteId) {
        //TODO: 사용자 검사 및 예외처리
        return ResponseEntity.ok()
                .body(voteService.findVoteParticipation(userId, voteId));
    }

    @Operation(summary = "투표하기", description = "투표하기, 예측 생성")
    @PostMapping("/vote")
    public ResponseEntity<String> createPrediction(Long userId,
                                                   CreatePredictionRequest createPredictionRequest) throws Exception {
        //TODO: 사용자 검사 및 예외처리
        if (voteService.createPrediction(userId, createPredictionRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "투표 예측", description = "기존에 투표만 한 투표에 포인트 예측")
    @PatchMapping("/prediction")
    public ResponseEntity<String> updatePrediction(Long userId,
                                                   UpdatePredictionRequest updatePredictionRequest) throws Exception {
        //TODO: 사용자 검사 및 예외처리
        if (voteService.updatePrediction(userId, updatePredictionRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "투표 삭제", description = "본인이 생성한 투표만 삭제 가능")
    @DeleteMapping("/{vote_id}")
    public ResponseEntity<String> deleteVote(Long userId,
                                             @PathVariable("vote_id") Long voteId) throws Exception {
        //TODO: 사용자 검사 및 예외처리
        if (voteService.deleteVote(userId, voteId)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }
}
