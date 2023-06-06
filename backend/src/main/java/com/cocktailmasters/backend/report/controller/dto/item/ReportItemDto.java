package com.cocktailmasters.backend.report.controller.dto.item;

import com.cocktailmasters.backend.report.domain.ReportedContentType;
import com.cocktailmasters.backend.report.domain.ReportedReasonType;
import com.cocktailmasters.backend.report.domain.entity.Report;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportItemDto {

    private long reportId;

    private Long reporterId;

    private ReportedContentType reportedContentType;

    private Long reportedContentId;

    private ReportedReasonType reportedReasonType;

    private String ReportedReasonDetail;

    private boolean isChecked;

    public static ReportItemDto createReportItem(Report report) {
        return ReportItemDto.builder()
                .reportId(report.getId())
                .reporterId(report.getUser().getId())
                .reportedContentType(report.getReportedContentType())
                .reportedContentId(report.getReportedContentId())
                .reportedContentType(report.getReportedContentType())
                .reportedContentId(report.getReportedContentId())
                .isChecked(report.isAllow())
                .build();
    }
}