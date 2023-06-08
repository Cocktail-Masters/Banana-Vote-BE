package com.cocktailmasters.backend.report.controller.dto;

import com.cocktailmasters.backend.report.domain.ReportedContentType;
import com.cocktailmasters.backend.report.domain.ReportedReasonType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportRequest {

    @NotNull
    private ReportedContentType reportedContentType;

    @NotNull
    private Long reportedContentId;

    @NotNull
    private ReportedReasonType reportedReasonType;

    private String ReportDetail;
}