package com.cocktailmasters.backend.report.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.report.controller.dto.item.ReportItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportResponse {
    @JsonProperty("total_page")
    private long totalPage;

    @JsonProperty("report_list")
    private List<ReportItemDto> reportList;
}
