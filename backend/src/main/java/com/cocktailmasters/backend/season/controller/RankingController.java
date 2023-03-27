package com.cocktailmasters.backend.season.controller;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.season.controller.dto.RankingResponse;
import com.cocktailmasters.backend.season.controller.dto.item.UserRanking;
import com.cocktailmasters.backend.season.service.RankingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "랭킹", description = "순위과 관련된 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {
    
    private final RankingService rankingService;

    private final String DEFAULT_PAGE_SIZE = "10";

    @Operation(summary = "랭킹 정보 조회",
        description = "일치된 시즌의 페이지에 해당하는 랭킹 정보 리스트 반환")
    @GetMapping("/{seasonId}")
    public ResponseEntity<RankingResponse> getRanking(@PathVariable long seasonId,
            @RequestParam(required = false, name = "page", defaultValue = "0") int page,
            @RequestParam(required = false, name = "size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
            // invalid query parameter
            if(page < 0 || pageSize <= 0)
                throw new InvalidParameterException("invalid page or page size");

            long totalPages = rankingService.getRankingTotalPages(seasonId, pageSize);
            List<UserRanking> userRankings = rankingService.getRankingWithPage(seasonId, page, pageSize);
        
            return ResponseEntity.ok(new RankingResponse(totalPages, userRankings));
    }
}
