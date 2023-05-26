package com.cocktailmasters.backend.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.report.controller.dto.ReportRequest;
import com.cocktailmasters.backend.report.controller.dto.ReportResponse;
import com.cocktailmasters.backend.report.service.ReportService;
import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "신고", description = "신고된 항목에 대한 정보를 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;
    private final JwtService jwtService;

    @Operation(summary = "컨텐츠를 신고", description = "(로그인 필요)", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping()
    public ResponseEntity<String> reportContent(@RequestHeader(name = "Authorization", required = false) String token,
            @RequestBody @Valid ReportRequest reportRequest) {

        long userId = jwtService.findUserByToken(token).getId();

        if (reportService.reportContent(reportRequest, userId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "컨텐츠를 체크", description = "이미 체크되어 있는 경우엔 따로 변화 없음, 에러 반환 X (로그인 필요)", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping()
    public ResponseEntity<String> checkContent(@PathVariable long reportId) {

        if (reportService.checkReport(reportId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "컨텐츠를 신고 내역을 조회(관리자용)", description = "(로그인 필요)", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME) })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<ReportResponse> getReports(
            @RequestParam(required = false, name = "check-option", defaultValue = "0") int checkOption,
            @RequestParam(required = false, name = "page", defaultValue = "0") int page,
            @RequestParam(required = false, name = "size", defaultValue = "10") int pageSize) {

        if (checkOption < 0 || checkOption > 2)
            checkOption = 0;

        ReportResponse reportResponse = reportService.getReportListWitPage(checkOption, page, pageSize);

        if (reportResponse.getReportList().isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(reportResponse);
    }
}
