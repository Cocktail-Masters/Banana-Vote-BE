package com.cocktailmasters.backend.ban.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.ban.controller.dto.BanLogResponse;
import com.cocktailmasters.backend.ban.controller.dto.BanRequest;
import com.cocktailmasters.backend.ban.service.BanService;
import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.xml.ws.Response;
import lombok.RequiredArgsConstructor;

@Tag(name = "밴", description = "밴과 관련된 기능들(관리자용)")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ban")
public class BanController {

    private final BanService banService;

    @Operation(summary = "밴 로그 조회(관리자용)", description = "밴 로그를 조회", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BanLogResponse>> getBanLogs() {
        List<BanLogResponse> banLogResponses = banService.getBanLogs();

        if (banLogResponses.size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(banLogResponses);
    }

    @Operation(summary = "사용자를 밴(관리자용)", description = "(관리자를 밴할 순 없음)", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> banUser(@PathVariable long userId, @RequestBody BanRequest banRequest) {
        if (banService.banUser(userId, banRequest.getBanReason()))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "사용자를 밴 해제(관리자용)", description = "제곧내", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping
    public ResponseEntity<List<BanLogResponse>> banUser(@PathVariable long userId) {

        return null;
    }
}
