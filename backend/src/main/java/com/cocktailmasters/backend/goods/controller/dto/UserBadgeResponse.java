package com.cocktailmasters.backend.goods.controller.dto;

import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.cocktailmasters.backend.goods.domain.entity.UserBadge;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserBadgeResponse {

    private long id;

    private String name;

    @JsonProperty("image_url")
    private String imageUrl;

    private String description;

    @JsonProperty("is_selling")
    private boolean isSelling;

    public static UserBadgeResponse createUserBadgeResponse(UserBadge userBadge) {
        Badge badgeInfo = userBadge.getBadge();

        return UserBadgeResponse.builder()
                .id(badgeInfo.getId())
                .name(badgeInfo.getBadgeName())
                .imageUrl(badgeInfo.getBadgeImageUrl())
                .description(badgeInfo.getBadgeDescription())
                .isSelling(badgeInfo.isSelling())
                .build();
    }
}
