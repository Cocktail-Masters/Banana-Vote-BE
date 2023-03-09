package com.cocktailmasters.backend.achievement.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "achievement_id"))
public class Achievement {

    @NotNull
    private String achievementName;

    @NotNull
    private String achievementDescription;

    @NotNull
    private String achievementImageUrl;

    @NotNull
    private int rewardPoint;
}
