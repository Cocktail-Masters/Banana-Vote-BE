package com.cocktailmasters.backend.goods.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.goods.controller.dto.BadgeRequest;
import com.cocktailmasters.backend.goods.service.BadgesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "뱃지", description = "뱃지와 관련된 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("/badges")
public class BadgeController {
    
    private final BadgesService badgeService;

    @Operation(summary = "뱃지 생성(관리자용)", description = "뱃지를 생성")
    @PostMapping
    public ResponseEntity<String> createBadge(@RequestBody @Valid BadgeRequest badgeRequest) {
        // TODO : 관리자 검증

        if(badgeService.makeBadge(badgeRequest)) 
            return ResponseEntity.created(null).build();
        else
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "뱃지 수정(관리자용)", description = "뱃지를 수정")
    @PatchMapping("/{badgeId}")
    public ResponseEntity<String> modifiyBadge(@PathVariable Long badgeId,
                                                @RequestBody @Valid BadgeRequest badgeRequest) {
        // TODO : 관리자 검증

        int res = badgeService.modifiyBadge(badgeId, badgeRequest);
        if(res == 1) 
            return ResponseEntity.ok().build();
        else if(res == 0)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "뱃지 삭제(관리자용)", description = "뱃지를 삭제")
    @DeleteMapping("/{badgeId}")
    public ResponseEntity<String> deleteBadge(@PathVariable Long badgeId) {
        // TODO : 관리자 검증
        
        if(badgeService.deleteBadge(badgeId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }
}
