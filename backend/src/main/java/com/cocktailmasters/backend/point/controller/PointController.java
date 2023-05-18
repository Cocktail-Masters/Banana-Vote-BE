package com.cocktailmasters.backend.point.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.point.controller.dto.PointLogResponse;
import com.cocktailmasters.backend.point.controller.dto.PointRequest;
import com.cocktailmasters.backend.point.service.PointLogService;
import com.cocktailmasters.backend.point.service.PointService;
import static com.cocktailmasters.backend.SwaggerConfig.SECURITY_SCHEME_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "포인트 로그 내역", description = "포인트 로그 내역 조회 + 포인트 관리(관리자용)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController {

    private final PointLogService pointLogService;
    private final PointService pointService;
    private final JwtService jwtService;

    @Operation(summary = "포인트 로그 조회", description = "포인트 로그 조회(로그인 필요)", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<PointLogResponse>> getPointLogs(
            @RequestHeader(name = "Authorization", required = false) String token) {
        long userId = jwtService.findUserByToken(token).getId();

        List<PointLogResponse> pointLogResponses = pointLogService.getPointLogs(userId);

        if (pointLogResponses.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(pointLogResponses);
    }

    @Operation(summary = "포인트 조회(관리자용)", description = "다른 사람의 포인트 조회", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<Long> getPoint(@PathVariable long userId) {
        long userPoint = pointService.getPoint(userId);

        if (userPoint == -1)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(userPoint);
    }

    @Operation(summary = "포인트 수정(관리자용)", description = "다른 사람의 포인트 수정", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{userId}")
    public ResponseEntity<String> modifyPoint(@PathVariable long userId, @RequestBody PointRequest points) {
        if (points == null)
            return ResponseEntity.badRequest().build();

        if (pointService.modifyPoint(points.getPoints(), userId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.noContent().build();
    }
}
