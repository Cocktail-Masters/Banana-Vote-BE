package com.cocktailmasters.backend.picket.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PicketConflictedResponse {

    @JsonProperty("last_modified")
    private String lastModified = "";

    @JsonProperty("current_price")
    private long currentPrice = -1;
}
