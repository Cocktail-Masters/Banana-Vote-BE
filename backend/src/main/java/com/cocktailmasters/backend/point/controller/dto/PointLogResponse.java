package com.cocktailmasters.backend.point.controller.dto;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.point.domain.entity.PointLog;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointLogResponse {
    private long amount;
    private String description;
    @JsonProperty("created_time")
    private String createdTime;

    public PointLogResponse(PointLog pointLog) {
        this.amount = pointLog.getAmount();
        this.description = pointLog.getDescription();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.createdTime = pointLog.getCreatedDate().format(formatter);
    }
}
