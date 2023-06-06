package com.cocktailmasters.backend.goods.controller.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BadgeRequest {

    @NotBlank
    private String name;

    @NotNull
    private String imageUrl;

    @NotNull
    private String description;

    @Builder.Default
    private boolean isSelling = true;

    @Builder.Default
    private LocalDate badgeEndDate = LocalDate.of(2100, 12, 31);

    @Builder.Default
    private long price = 0L;
}
