package com.cocktailmasters.backend.goods.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.goods.controller.dto.BadgeRequest;
import com.cocktailmasters.backend.goods.controller.dto.BadgeResponse;
import com.cocktailmasters.backend.goods.controller.dto.UserBadgeResponse;
import com.cocktailmasters.backend.goods.service.BadgesService;
import com.cocktailmasters.backend.goods.service.UserBadgeService;

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
    private final UserBadgeService userBadgeService;

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

    @Operation(summary = "뱃지 리스트 반환", description = "판매하는 뱃지 true, 아니면 false, 기본은 false, 판매 기한이 지난 뱃지도 제외")
    @GetMapping
    public ResponseEntity<List<BadgeResponse>> getBadgeList(@RequestParam(required = false, name = "selling", defaultValue = "false") boolean isSelling) {
        List<BadgeResponse> badges = badgeService.getBadgeList(isSelling);

        if(badges.size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(badges);
    }

    @Operation(summary = "유저가 소유 중인 뱃지 리스트 반환", description = "유저가 소유 중인 뱃지 리스트 반환 (로그인 필요)")
    @GetMapping("/users")
    public ResponseEntity<List<UserBadgeResponse>> getUsersBadgeList() {
        // TODO : check login validation logic
        long userId = 1L;

        List<UserBadgeResponse> badgeResponses = userBadgeService.getUserBadgesList(userId);

        if(badgeResponses.size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(badgeResponses);
    }

    @Operation(summary = "유저에게 해당 뱃지를 추가(관리자용)", description = "해당 유저에게 뱃지를 추가, 유저가 없거나 추가할 뱃지가 없거나, \n해당 유저가 이미 그 뱃지를 가지고 있을 경우 X")
    @PostMapping("/users/{badgeId}")
    public ResponseEntity<String> addBadgeToUser(@PathVariable long badgeId,
                                                @RequestParam(required = true, name = "userId") long userId) {
        // TODO : 관리자 검증
        int res = userBadgeService.addBadgeToUser(badgeId, userId);

        if(res == 1)
            return ResponseEntity.created(null).build();                  
        else if(res == 0)
            return ResponseEntity.notFound().build();                                
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Operation(summary = "유저에게 해당 뱃지를 삭제(관리자용)", description = "해당 유저에게 뱃지를 삭제, 유저가 없거나 삭제할 뱃지가 없거나")
    @DeleteMapping("/users/{badgeId}")
    public ResponseEntity<String> deleteBadgeToUser(@PathVariable long badgeId, 
    @RequestParam(required = true, name = "userId") long userId) {
        // TODO : 관리자 검증

        if(userBadgeService.deleteBadgeFromUser(badgeId, userId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }
}
