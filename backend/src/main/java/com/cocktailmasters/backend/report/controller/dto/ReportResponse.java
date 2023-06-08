package com.cocktailmasters.backend.report.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.report.controller.dto.item.ReportItemDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportResponse {

    private long totalPage;

    private List<ReportItemDto> reportList;
}
