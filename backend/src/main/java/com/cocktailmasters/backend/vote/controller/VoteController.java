package com.cocktailmasters.backend.vote.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.user.domain.entity.Role;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.point.service.PointService;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemCreateDto;
import com.cocktailmasters.backend.vote.controller.dto.vote.*;
import com.cocktailmasters.backend.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "투표", description = "투표 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/votes")
public class VoteController {

    private final JwtService jwtService;
    private final PointService pointService;
    private final VoteService voteService;

    @Operation(summary = "투표 생성",
            description = "새로운 투표 생성(투표 종료 날짜는 하루 이상부터), 투표 항목 필수",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("")
    public ResponseEntity<String> createVote(@RequestHeader(name = "Authorization", required = false) String token,
                                             @Valid @RequestBody CreateVoteRequest createVoteRequest) {
        User user = jwtService.findUserByToken(token);
        if (createVoteRequest.getIsEvent() && user.getRole() != Role.ADMIN) {
            return ResponseEntity.badRequest().build();
        }
        if (createVoteRequest.getVoteItems().isEmpty() || createVoteRequest.getVoteEndDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().build();
        }
        Set<Integer> voteItemSet = new HashSet<>();
        for (VoteItemCreateDto voteItem : createVoteRequest.getVoteItems()) {
            if (voteItemSet.contains(voteItem.getItemNumber())) {
                return ResponseEntity.badRequest().build();
            }
            voteItemSet.add(voteItem.getItemNumber());
        }
        if (voteService.createVote(user, createVoteRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "투표 수정",
            description = "이벤트 투표 수정(관리자 전용), 투표 삭제 후 새로 생성, 투표한 포인트는 반환",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/votes/{vote_id}")
    public ResponseEntity<String> updateEventVote(@RequestHeader(name = "Authorization", required = false) String token,
                                                  @RequestParam("vote_id") long voteId,
                                                  @RequestBody CreateVoteRequest createVoteRequest) {
        User user = jwtService.findUserByToken(token);
        if (createVoteRequest.getVoteItems().isEmpty() || createVoteRequest.getVoteEndDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().build();
        }
        Set<Integer> voteItemSet = new HashSet<>();
        for (VoteItemCreateDto voteItem : createVoteRequest.getVoteItems()) {
            if (voteItemSet.contains(voteItem.getItemNumber())) {
                return ResponseEntity.badRequest().build();
            }
            voteItemSet.add(voteItem.getItemNumber());
        }
        if (voteService.updateEventVote(user, createVoteRequest, voteId)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "투표 검색", description = "검색어를 사용하여 투표 검색," +
            "검색 옵션은 정렬기준, 종료유무, 페이지 인덱스, 10개 반환",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @GetMapping("/options")
    public ResponseEntity<FindVotesResponse> findVotes(@RequestHeader(name = "Authorization", required = false) String token,
                                                       Pageable pageable,
                                                       @RequestParam("keyword") String keyword,
                                                       @RequestParam(value = "is-tag", defaultValue = "false") boolean isTag,
                                                       @RequestParam(value = "is-closed", defaultValue = "false") boolean isClosed,
                                                       @RequestParam(value = "is-event", defaultValue = "false") boolean isEvent,
                                                       @RequestParam(value = "sort-by", defaultValue = "1") int sortBy) {
        User user = null;
        if (token != null) {
            user = jwtService.findUserByToken(token);
        }
        return ResponseEntity.ok()
                .body(voteService.findVotes(user, keyword, isTag, isClosed, isEvent, sortBy, pageable));
    }

    @Operation(summary = "투표글 상세 보기", description = "투표글 상세 보기, 투표 조회수 증가")
    @GetMapping("/{vote_id}")
    public ResponseEntity<FindVoteDetailResponse> findVoteDetail(@PathVariable("vote_id") Long voteId) {
        return ResponseEntity.ok()
                .body(voteService.findVoteDetail(voteId));
    }

    @Operation(summary = "투표 유무 확인", description = "투표 유무 확인",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/check/{vote_id}")
    public ResponseEntity<FindVoteParticipationResponse> findVoteParticipation(@RequestHeader(name = "Authorization", required = false) String token,
                                                                               @PathVariable("vote_id") Long voteId) {
        User user = jwtService.findUserByToken(token);
        return ResponseEntity.ok()
                .body(voteService.findVoteParticipation(user, voteId));
    }

    @Operation(summary = "투표하기", description = "투표하기, 예측 생성",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/vote")
    public ResponseEntity<String> createPrediction(@RequestHeader(name = "Authorization", required = false) String token,
                                                   @Valid @RequestBody CreatePredictionRequest createPredictionRequest) throws Exception {
        User user = jwtService.findUserByToken(token);
        if (pointService.getPoint(user.getId()) < createPredictionRequest.getVote().getPoints()) {
            return ResponseEntity.badRequest().build();
        }
        if (voteService.createPrediction(user, createPredictionRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "투표 예측", description = "기존에 투표만 한 투표에 포인트 예측",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/prediction")
    public ResponseEntity<String> updatePrediction(@RequestHeader(name = "Authorization", required = false) String token,
                                                   @RequestBody UpdatePredictionRequest updatePredictionRequest) throws Exception {
        User user = jwtService.findUserByToken(token);
        if (pointService.getPoint(user.getId()) < updatePredictionRequest.getPrediction().getPoints()) {
            return ResponseEntity.badRequest().build();
        }
        if (voteService.updatePrediction(user, updatePredictionRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "투표 삭제", description = "본인이 생성한 투표만 삭제 가능",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{vote_id}")
    public ResponseEntity<String> deleteVote(@RequestHeader(name = "Authorization", required = false) String token,
                                             @PathVariable("vote_id") Long voteId) throws Exception {
        jwtService.findUserByToken(token);
        if (voteService.deleteVote(voteId)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "진행중인 인기 투표 리스트 조회", description = "진행중인 인기 투표 리스트를 5개 반환")
    @GetMapping("/popular")
    public ResponseEntity<FindPopularVotesResponse> findPopularVotes() {
        return ResponseEntity.ok()
                .body(voteService.findPopularVotes());
    }

    @Operation(summary = "관심 있을만한 최신 투표 리스트 조회", description = "관심 있을만한 최신 투표 리스트를 최소 5개 반환",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/interest")
    public ResponseEntity<FindInterestVotesResponse> findInterestVotes(@RequestHeader(name = "Authorization", required = false) String token) {
        User user = jwtService.findUserByToken(token);
        return ResponseEntity.ok()
                .body(voteService.findInterestVotes(user));
    }

    @Operation(summary = "투표 예측 리스트 확인", description = "투표 예측 리스트 확인",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{vote_id}/prediction/")
    public ResponseEntity<FindPredictionsResponse> findPredictions(@RequestHeader(name = "Authorization", required = false) String token,
                                                                   @PathVariable("vote_id") Long voteId) {
        User user = jwtService.findUserByToken(token);
        return ResponseEntity.ok()
                .body(voteService.findPredictions(voteId));
    }
}
