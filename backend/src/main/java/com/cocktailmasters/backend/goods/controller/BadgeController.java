package com.cocktailmasters.backend.goods.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.goods.controller.dto.BadgeRequest;
import com.cocktailmasters.backend.goods.controller.dto.BadgeResponse;
import com.cocktailmasters.backend.goods.controller.dto.UserBadgeResponse;
import com.cocktailmasters.backend.goods.service.BadgeService;
import com.cocktailmasters.backend.goods.service.UserBadgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "뱃지", description = "뱃지와 관련된 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("/badges")
public class BadgeController {

    private final JwtService jwtService;
    private final BadgeService badgeService;
    private final UserBadgeService userBadgeService;

    @Operation(summary = "뱃지 생성(관리자용)", description = "뱃지를 생성", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> createBadge(@RequestBody @Valid BadgeRequest badgeRequest) {
        if (badgeService.makeBadge(badgeRequest))
            return ResponseEntity.created(null).build();
        else
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "뱃지 구매", description = "뱃지를 구매(로그인 필요), 포인트 부족, 뱃지 존재 X 시 에러 코드", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping("/{badgeId}")
    public ResponseEntity<String> buyBadge(
            @RequestHeader(name = "Authorization", required = false) String token,
            @PathVariable long badgeId) {
        long userId = jwtService.findUserByToken(token).getId();

        int result = badgeService.buyBadge(badgeId, userId);
        if (result == 1)
            return ResponseEntity.ok().build();
        else if (result == 0)
            return ResponseEntity.notFound().build();
        else // result == -1
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "뱃지 수정(관리자용)", description = "뱃지를 수정", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{badgeId}")
    public ResponseEntity<String> modifiyBadge(@PathVariable Long badgeId,
            @RequestBody @Valid BadgeRequest badgeRequest) {
        int res = badgeService.modifiyBadge(badgeId, badgeRequest);
        if (res == 1)
            return ResponseEntity.ok().build();
        else if (res == 0)
            return ResponseEntity.badRequest().build();
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "뱃지 삭제(관리자용)", description = "뱃지를 삭제", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{badgeId}")
    public ResponseEntity<String> deleteBadge(@PathVariable Long badgeId) {
        if (badgeService.deleteBadge(badgeId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "뱃지 리스트 반환", description = "판매하는 뱃지 true, 아니면 false, 기본은 false, 판매 기한이 지난 뱃지도 제외\n")
    @GetMapping
    public ResponseEntity<BadgeResponse> getBadgeList(
            @RequestParam(required = false, name = "selling", defaultValue = "false") boolean isSelling,
            @RequestParam(required = false, name = "sortby", defaultValue = "1") int sortBy,
            @RequestParam(required = false, name = "page", defaultValue = "0") int page,
            @RequestParam(required = false, name = "size", defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize < 1)
            return ResponseEntity.badRequest().build();

        BadgeResponse badgeResponse = badgeService.getBadgeList(isSelling, sortBy, page, pageSize);

        if (badgeResponse.getBadgeList().size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(badgeResponse);
    }

    @Operation(summary = "유저가 소유 중인 뱃지 리스트 반환", description = "유저가 소유 중인 뱃지 리스트 반환 (로그인 필요)", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserBadgeResponse>> getUsersBadgeList(
            @RequestHeader(name = "Authorization", required = false) String token) {
        long userId = jwtService.findUserByToken(token).getId();

        List<UserBadgeResponse> badgeResponses = userBadgeService.getUserBadgesList(userId);

        if (badgeResponses.size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(badgeResponses);
    }

    @Operation(summary = "유저에게 해당 뱃지를 추가(관리자용)", description = "해당 유저에게 뱃지를 추가, 유저가 없거나 추가할 뱃지가 없거나, \n해당 유저가 이미 그 뱃지를 가지고 있을 경우 X", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/users/{badgeId}")
    public ResponseEntity<String> addBadgeToUser(@PathVariable long badgeId,
            @RequestParam(required = true, name = "userId") long userId) {
        int res = userBadgeService.addBadgeToUser(badgeId, userId);

        if (res == 1)
            return ResponseEntity.created(null).build();
        else if (res == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @Operation(summary = "유저에게 해당 뱃지를 삭제(관리자용)", description = "해당 유저에게 뱃지를 삭제, 유저가 없거나 삭제할 뱃지가 없거나", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{badgeId}")
    public ResponseEntity<String> deleteBadgeToUser(@PathVariable long badgeId,
            @RequestParam(required = true, name = "userId") long userId) {
        if (userBadgeService.deleteBadgeFromUser(badgeId, userId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }
}
