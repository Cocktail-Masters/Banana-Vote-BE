package com.cocktailmasters.backend.point.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointRequest {
    @JsonProperty("points")
    private long points;
}
