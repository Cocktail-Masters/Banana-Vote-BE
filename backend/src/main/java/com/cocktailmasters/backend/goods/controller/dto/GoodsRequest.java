package com.cocktailmasters.backend.goods.controller.dto;

import java.time.LocalDate;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoodsRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private GoodsType type;

    @NotNull
    @Min(value = 0, message = "음수 가격은 허용하지 않습니다")
    private long price;

    private String imageUrl;

    private long validPeriod;

    private LocalDate startDate;

    private LocalDate endDate;

    private long remainingQuantity;
}
