package com.cocktailmasters.backend.achievement.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "achievement_id"))
@Entity
public class Achievement extends BaseEntity {

    @NotNull
    private String achievementName;

    @NotNull
    private String achievementDescription;

    @NotNull
    private String achievementImageUrl;

    @NotNull
    private int rewardPoint;

    @Builder.Default
    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL)
    private List<UserAchievement> userAchievements = new ArrayList<>();
}
