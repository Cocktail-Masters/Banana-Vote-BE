package com.cocktailmasters.backend.megaphone.controller;

import com.cocktailmasters.backend.megaphone.domain.dto.MegaphoneResponse;
import com.cocktailmasters.backend.megaphone.service.MegaphoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

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

    @Operation(summary = "확성기 지우기(관리자용)", description = "확성기 목록 중에 삭제", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{megaphoneId}")
    public ResponseEntity<String> deleteMegaphones(@PathVariable long megaphoneId) {
        if (!megaphoneService.deleteMegaphones(megaphoneId))
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().build();
    }
}
