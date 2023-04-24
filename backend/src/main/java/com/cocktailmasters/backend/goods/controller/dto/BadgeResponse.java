package com.cocktailmasters.backend.goods.controller.dto;

import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BadgeResponse {
    
    private long id;

    private String name;

    @JsonProperty("image_url")
    private String imageUrl;

    private String desciption;

    private long price;

    @JsonProperty("sold_count")
    private long soldCount;

    @JsonProperty("is_selling")
    private boolean isSelling;

    public static BadgeResponse createBadgeReponse(Badge badge) {
        return BadgeResponse.builder()
                .id(badge.getId())
                .name(badge.getBadgeName())
                .imageUrl(badge.getBadgeImageUrl())
                .desciption(badge.getBadgeDescription())
                .price(badge.getBadgePrice())
                .soldCount(badge.getBadgeSoldNumber())
                .isSelling(badge.isSelling())
                .build();
    }
}
