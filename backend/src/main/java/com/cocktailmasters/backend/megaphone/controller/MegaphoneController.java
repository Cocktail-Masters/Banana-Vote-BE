package com.cocktailmasters.backend.megaphone.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.megaphone.domain.dto.MegaphoneResponse;
import com.cocktailmasters.backend.megaphone.service.MegaphoneService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "확성기 사용", description = "확성기에 대한 여러 작업들")
@RequiredArgsConstructor
@RestController
@RequestMapping("/megaphones")
public class MegaphoneController {

    private final MegaphoneService megaphoneService;

    @Operation(summary = "확성기 정보", description = "현재 유저들에게 보여지고 있는 확성기 정보, active false는 기한이 지난 모든 확성기")
    @GetMapping
    public ResponseEntity<List<MegaphoneResponse>> getMegaphones(
            @RequestParam(required = false, defaultValue = "true") boolean active) {
        List<MegaphoneResponse> megaphoneResponses = megaphoneService.getMegaphones(active);

        if (megaphoneResponses.isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(megaphoneResponses);
    }

    @Operation(summary = "확성기 지우기(관리자용)", description = "확성기 목록 중에 삭제")
    @DeleteMapping("/{megaphoneId}")
    public ResponseEntity<String> deleteMegaphones(@PathVariable long megaphoneId) {
        if (!megaphoneService.deleteMegaphones(megaphoneId))
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().build();
    }
}
