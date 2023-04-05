package com.cocktailmasters.backend.season.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.season.controller.dto.SeasonDto;
import com.cocktailmasters.backend.season.domain.entity.Season;
import com.cocktailmasters.backend.season.service.SeasonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "시즌", description = "시즌과 관련된 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("/season")
public class SeasonController {

    private final SeasonService seasonService;
    
    @Operation(summary = "시즌 정보 조회",
        description = "현재 시즌의 정보(겹치는 시즌이 여러 개 일 경우 먼저 시작한 첫번째 시즌) 또는 지금까지 모든 시즌의 정보 반환")
    @GetMapping
    public ResponseEntity<List<SeasonDto>> getSeasonInfo(@RequestParam(required = false, name = "current", defaultValue = "false") String isCurrent) {
        if(isCurrent.equals("true")) {
            // only current season
            Season currentSeason = seasonService.getCurrentSeason();
            
            if(currentSeason == null)
                return ResponseEntity.noContent().build();
            else 
                return ResponseEntity.ok().body(Arrays.asList(new SeasonDto(currentSeason)));
                
        } else {
            // all seasons
            List<Season> allSeasons = seasonService.getSeasons();
            List<SeasonDto> seasonDtos = new ArrayList<>();
            for(Season season : allSeasons)
                seasonDtos.add(new SeasonDto(season));

            if(seasonDtos.isEmpty())
                return ResponseEntity.noContent().build();
            else
                return ResponseEntity.ok().body(seasonDtos);
        }
    }

    @Operation(summary = "시즌 정보를 추가(관리자용) - id는 신경 안써도 됨",
        description = "현재 시즌의 정보에 대해 추가, 만약 일자가 겹칠 경우엔 에러 발생")
    @PostMapping
    public ResponseEntity<String> addSeasonInfo(@Valid @RequestBody SeasonDto season) {
        // TODO : add admin check logic

        // check invalid date request
        if(season.getEndDate().isBefore(season.getStartDate())) 
            return ResponseEntity.badRequest().body("invalid start and end date");
        if(season.getStartDate() == null || season.getEndDate() == null)
            return ResponseEntity.badRequest().body("start or end date is null");

        // add season info
        if(seasonService.addSeason(season, true))
            return ResponseEntity.created(null).build();
        else
            return ResponseEntity.badRequest().body("conflicted date");
    }

    @Operation(summary = "시즌 정보를 수정(관리자용)",
        description = "현재 시즌의 정보에 대해 수정, 일부 필드에 대해서만 수정하는 경우도 가능")
    @PatchMapping
    public ResponseEntity<String> patchSeasonInfo(@Valid @RequestBody SeasonDto season) {
        // TODO : add admin check logic

        if(seasonService.modifySeason(season, true))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("invalid data(conflicted date or non-existed id)");
    }
}
