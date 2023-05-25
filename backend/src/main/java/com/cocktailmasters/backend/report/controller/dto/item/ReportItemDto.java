package com.cocktailmasters.backend.report.controller.dto.item;

import com.cocktailmasters.backend.report.domain.ReportedContentType;
import com.cocktailmasters.backend.report.domain.ReportedReasonType;
import com.cocktailmasters.backend.report.domain.entity.Report;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportItemDto {

    @JsonProperty("report_id")
    private long reportId;

    @JsonProperty("reporter_id")
    private Long reporterId;

    @JsonProperty("reported_content_type")
    private ReportedContentType reportedContentType;

    @JsonProperty("reported_content_id")
    private Long reportedContentId;

    @JsonProperty("reported_content_id")
    private ReportedReasonType reportedReasonType;

    @JsonProperty("reported_reason_detail")
    private String ReportedReasonDetail;

    public static ReportItemDto createReportItem(Report report) {
        return ReportItemDto.builder()
                .reportId(report.getId())
                .reporterId(report.getUser().getId())
                .reportedContentType(report.getReportedContentType())
                .reportedContentId(report.getReportedContentId())
                .reportedContentType(report.getReportedContentType())
                .reportedContentId(report.getReportedContentId())
                .build();
    }
}