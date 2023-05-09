package com.cocktailmasters.backend.goods.controller.dto;

import java.time.LocalDate;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private GoodsType type;

    @NotNull
    private long price;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("use_period")
    private long validPeriod;

    @JsonProperty("start_date")
    private LocalDate startDate;

    @JsonProperty("end_date")
    private LocalDate endDate;

    @JsonProperty("remaining_quantity")
    private long remainingQuantity;
}
