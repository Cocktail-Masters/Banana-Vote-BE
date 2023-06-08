package com.cocktailmasters.backend.goods.controller.dto.item;

import java.time.LocalDate;

import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BadgeItemDto {

    private long id;

    private String name;

    private String imageUrl;

    private String description;

    private long price;

    private long soldCount;

    private boolean isSelling;

    private LocalDate endDate;

    public static BadgeItemDto createBadgeReponse(Badge badge) {
        return BadgeItemDto.builder()
                .id(badge.getId())
                .name(badge.getBadgeName())
                .imageUrl(badge.getBadgeImageUrl())
                .description(badge.getBadgeDescription())
                .price(badge.getBadgePrice())
                .soldCount(badge.getBadgeSoldNumber())
                .isSelling(badge.isSelling())
                .endDate(badge.getBadgeEndDate())
                .build();
    }
}
