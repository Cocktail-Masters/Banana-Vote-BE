package com.cocktailmasters.backend.season.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.season.controller.dto.RankingResponse;
import com.cocktailmasters.backend.season.service.RankingService;
import com.cocktailmasters.backend.season.service.SeasonService;
import com.cocktailmasters.backend.util.exception.CustomException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "랭킹", description = "순위과 관련된 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {
    
    private final RankingService rankingService;

    private final SeasonService seasonService;

    private final String DEFAULT_PAGE_SIZE = "10";

    @Operation(summary = "랭킹 정보 조회",
        description = "일치된 시즌의 페이지에 해당하는 랭킹 정보 리스트 반환, 닉네임을 포함 할 경우 페이지 파라미터는 무효되고, 페이지 사이즈에 맞추어 해당 플레이어가 포함된 결과 반환")
    @GetMapping(value = {"/{seasonId}", ""})
    public ResponseEntity<RankingResponse> getRanking(@PathVariable(required = false) Optional<Long> seasonId,
            @RequestParam(required = false, name = "page", defaultValue = "0") int page,
            @RequestParam(required = false, name = "size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(required = false, name = "nickname") String nickname) {
            long seasonIdNum = seasonId.orElse(seasonService.getLatestSeason().getId());

            if(seasonIdNum == -1) // check season is existed
                throw new CustomException(HttpStatus.NOT_FOUND, "there is no season");    
            if(page < 0 || pageSize < 1)
                throw new CustomException(HttpStatus.BAD_REQUEST, "bad page or page size request");

            if(nickname != null && !nickname.isBlank()) {
                page = rankingService.getRankingPageNumberByNickname(seasonIdNum, pageSize, nickname);
                if(page == -1) // nickname not found
                    return ResponseEntity.noContent().build();
            }
            
            RankingResponse rankingResponse = rankingService.getRankingListWithPage(seasonIdNum, page, pageSize);

            if(rankingResponse.getRankingList() == null || rankingResponse.getRankingList().isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(rankingResponse);
    }
}
