package com.cocktailmasters.backend.goods.controller.dto.item;

import java.time.LocalDate;

import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadgeItemDto {

    private long id;

    private String name;

    @JsonProperty("image_url")
    private String imageUrl;

    private String description;

    private long price;

    @JsonProperty("sold_count")
    private long soldCount;

    @JsonProperty("is_selling")
    private boolean isSelling;

    @JsonProperty("end_date")
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
