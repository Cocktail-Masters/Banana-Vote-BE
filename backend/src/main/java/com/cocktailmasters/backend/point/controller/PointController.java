package com.cocktailmasters.backend.point.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.point.controller.dto.PointLogResponse;
import com.cocktailmasters.backend.point.controller.dto.PointRequest;
import com.cocktailmasters.backend.point.service.PointLogService;
import com.cocktailmasters.backend.point.service.PointService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "포인트 로그 내역", description = "포인트 로그 내역 조회 + 포인트 관리(관리자용)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController {
    
    private final PointLogService pointLogService;
    private final PointService pointService;

    @Operation(summary = "포인트 로그 조회",
        description = "포인트 로그 조회(로그인 필요)")
    @GetMapping
    public ResponseEntity<List<PointLogResponse>> getPointLogs(@RequestHeader("Authorization") String token) {
        // TODO : add JWT token validation and extract user id by token
        long userId = 4L; // token.substring(7);

        List<PointLogResponse> pointLogResponses = pointLogService.getPointLogs(userId);

        if(pointLogResponses.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(pointLogResponses);
    }

    @Operation(summary = "포인트 조회(관리자용)",
        description = "다른 사람의 포인트 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<Long> getPoint(@PathVariable long userId) {
        // TODO : admin check logic

        long userPoint = pointService.getPoint(userId);

        if(userPoint == -1)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(userPoint);
    }

    @Operation(summary = "포인트 증가 및 감소(관리자용)",
        description = "다른 사람의 포인트 수정, ")
    @PatchMapping("/{userId}")
    public ResponseEntity<String> modifyPoint(@PathVariable long userId, @RequestBody PointRequest points) {
        // TODO : admin check logic

        if(points == null) return ResponseEntity.badRequest().build();

        if(pointService.addPoint(points.getPoints(), "modified by admin", userId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.noContent().build();
    }
}
