package com.cocktailmasters.backend.picket.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PicketRequest {

    private int position = -1;

    private String picketImageUrl = "";

    private long curPrice = -1;

    private long paidPrice = -1;
}
