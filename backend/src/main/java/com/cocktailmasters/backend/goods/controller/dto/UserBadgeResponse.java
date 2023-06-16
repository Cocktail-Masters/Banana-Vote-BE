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

    private Long badgeId;

    private String name;

    private String imageUrl;

    private String description;

    private boolean isEquipped;

    public static UserBadgeResponse createUserBadgeResponse(UserBadge userBadge) {
        Badge badgeInfo = userBadge.getBadge();

        return UserBadgeResponse.builder()
                .id(userBadge.getId())
                .badgeId(badgeInfo.getId())
                .name(badgeInfo.getBadgeName())
                .imageUrl(badgeInfo.getBadgeImageUrl())
                .description(badgeInfo.getBadgeDescription())
                .isEquipped(userBadge.isEquipped())
                .build();
    }
}
