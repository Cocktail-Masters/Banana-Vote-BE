package com.cocktailmasters.backend.goods.controller.dto;

import java.time.LocalDate;

import org.springframework.cglib.core.Local;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class BadgeRequest {
    
    @NotBlank
    private String name;

    @NotNull
    @JsonProperty("image_url")
    private String imageUrl;

    @NotNull
    private String description;

    @Builder.Default
    @JsonProperty("is_selling")
    private boolean isSelling = true;

    @Builder.Default
    @JsonProperty("end_date")
    private LocalDate badgeEndDate = LocalDate.of(2100, 12, 31);

    @Builder.Default
    private long price = 0L;
}
