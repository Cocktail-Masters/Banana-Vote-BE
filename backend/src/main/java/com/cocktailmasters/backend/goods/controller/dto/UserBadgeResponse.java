package com.cocktailmasters.backend.goods.controller.dto;

import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.cocktailmasters.backend.goods.domain.entity.UserBadge;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserBadgeResponse {

    private long id;

    private String name;

    private String imageUrl;

    private String description;

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
