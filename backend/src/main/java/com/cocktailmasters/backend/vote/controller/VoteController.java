package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemCreateDto;
import com.cocktailmasters.backend.vote.controller.dto.vote.*;
import com.cocktailmasters.backend.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.cocktailmasters.backend.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "vote", description = "투표 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/votes")
public class VoteController {

    private final JwtService jwtService;
    private final VoteService voteService;

    @Operation(summary = "투표 생성", description = "새로운 투표 생성",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @PostMapping("")
    public ResponseEntity<String> createVote(@RequestHeader("Authorization") String token,
                                             @Valid @RequestBody CreateVoteRequest createVoteRequest) throws Exception {
        User user = jwtService.findUserByToken(token);
        if (createVoteRequest.getVoteItems().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Set<Integer> voteItemSet = new HashSet<>();
        for (VoteItemCreateDto voteItem : createVoteRequest.getVoteItems()) {
            if (voteItemSet.contains(voteItem.getItemNumber())) {
                return ResponseEntity.badRequest().build();
            }
            voteItemSet.add(voteItem.getItemNumber());
        }
        if (createVoteRequest.getVoteEndDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().build();
        }
        if (voteService.createVote(user, createVoteRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "투표 검색", description = "검색어를 사용하여 투표 검색," +
            "검색 옵션은 정렬기준, 종료유무, 페이지 인덱스, 10개 반환")
    @GetMapping("/{page_index}/options")
    public ResponseEntity<FindVotesResponse> findVotes(@PathVariable("page_index") int pageIndex,
                                                       @RequestParam("keyword") String keyword,
                                                       @RequestParam(value = "is-tag", defaultValue = "false") boolean isTag,
                                                       @RequestParam(value = "is-closed", defaultValue = "false") boolean isClosed,
                                                       @RequestParam(value = "sort-by", defaultValue = "1") int sortBy) {
        // TODO: 사용자 검사 필요
        Long userId = 1L;
        PageRequest page = PageRequest.of(pageIndex, 10);
        return ResponseEntity.ok()
                .body(voteService.findVotes(userId, keyword, isTag, isClosed, sortBy, page));
    }

    @Operation(summary = "투표글 상세 보기", description = "투표글 상세 보기, 투표 조회수 증가")
    @GetMapping("/{vote_id}")
    public ResponseEntity<FindVoteDetailResponse> findVoteDetail(@PathVariable("vote_id") Long voteId) {
        //TODO: 사용자 검사 및 예외처리
        Long userId = 1L;
        return ResponseEntity.ok()
                .body(voteService.findVoteDetail(voteId));
    }

    @Operation(summary = "투표 유무 확인", description = "투표 유무 확인")
    @GetMapping("/check/{vote_id}")
    public ResponseEntity<FindVoteParticipationResponse> findVoteParticipation(@PathVariable("vote_id") Long voteId) {
        //TODO: 사용자 검사 및 예외처리
        Long userId = 1L;
        return ResponseEntity.ok()
                .body(voteService.findVoteParticipation(userId, voteId));
    }

    @Operation(summary = "투표하기", description = "투표하기, 예측 생성")
    @PostMapping("/vote")
    public ResponseEntity<String> createPrediction(@RequestBody CreatePredictionRequest createPredictionRequest) throws Exception {
        //TODO: 사용자 검사 및 예외처리
        Long userId = 1L;
        if (voteService.createPrediction(userId, createPredictionRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "투표 예측", description = "기존에 투표만 한 투표에 포인트 예측")
    @PatchMapping("/prediction")
    public ResponseEntity<String> updatePrediction(@RequestBody UpdatePredictionRequest updatePredictionRequest) throws Exception {
        //TODO: 사용자 검사 및 예외처리
        Long userId = 1L;
        if (voteService.updatePrediction(userId, updatePredictionRequest)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "투표 삭제", description = "본인이 생성한 투표만 삭제 가능")
    @DeleteMapping("/{vote_id}")
    public ResponseEntity<String> deleteVote(@PathVariable("vote_id") Long voteId) throws Exception {
        //TODO: 사용자 검사 및 예외처리
        Long userId = 1L;
        if (voteService.deleteVote(userId, voteId)) {
            return ResponseEntity.created(null).build();
        }
        throw new Exception();
    }

    @Operation(summary = "진행중인 인기 투표 리스트 조회", description = "진행중인 인기 투표 리스트를 5개 반환")
    @GetMapping("/popular")
    public ResponseEntity<FindPopularVotesResponse> findPopularVotes() {
        //TODO: 예외처리
        Long userId = 1L;
        return ResponseEntity.ok()
                .body(voteService.findPopularVotes());
    }

    @Operation(summary = "관심 있을만한 최신 투표 리스트 조회", description = "관심 있을만한 최신 투표 리스트를 최소 5개 반환")
    @GetMapping("/interest")
    public ResponseEntity<FindInterestVotesResponse> findInterestVotes() {
        //TODO: 사용자 검사 및 예외처리
        Long userId = 1L;
        return ResponseEntity.ok()
                .body(voteService.findInterestVotes(userId));
    }

    @Operation(summary = "관심 있을만한 최신 투표 리스트 조회", description = "관심 있을만한 최신 투표 리스트를 최소 5개 반환")
    @GetMapping("/{vote_id}/prediction/")
    public ResponseEntity<FindPredictionsResponse> findPredictions(@PathVariable("vote_id") Long voteId) {
        //TODO: 사용자 검사 및 예외처리
        Long userId = 1L;
        return ResponseEntity.ok()
                .body(voteService.findPredictions(voteId));
    }
}
