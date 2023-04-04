package com.cocktailmasters.backend.point.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.point.controller.dto.PointLogResponse;
import com.cocktailmasters.backend.point.service.PointLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name = "포인트 로그 내역", description = "포인트 로그 내역 조회 + 포인트 관리(관리자용)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController {
    
    private final PointLogService pointLogService;

    @Operation(summary = "포인트 로그 조회",
        description = "포인트 로그 조회(로그인 필요)")
    @GetMapping
    public ResponseEntity<List<PointLogResponse>> getPointLogs(@RequestHeader("Authorization") String token) {
        // TODO : add JWT token validation and extract user id by token
        long userId = 0L; // token.substring(7);

        List<PointLogResponse> pointLogResponses = pointLogService.getPointLogs(userId);

        if(pointLogResponses.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(pointLogResponses);
    }

    
    public ResponseEntity<Long> getPoint() {
        // TODO : 관리자 확인 로직

    }
}
