package com.cocktailmasters.backend.report.controller.dto;

import com.cocktailmasters.backend.report.domain.ReportedContentType;
import com.cocktailmasters.backend.report.domain.ReportedReasonType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {

    @NotNull
    private ReportedContentType reportedContentType;

    @NotNull
    private Long reportedContentId;

    @NotNull
    private ReportedReasonType reportedReasonType;

    private String ReportDetail;
}