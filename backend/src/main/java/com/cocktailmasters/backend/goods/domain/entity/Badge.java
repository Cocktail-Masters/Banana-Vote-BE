package com.cocktailmasters.backend.goods.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.goods.controller.dto.BadgeRequest;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "badge_id"))
@Entity
public class Badge extends BaseEntity {

    @NotNull
    private String badgeName;

    @NotNull
    private String badgeImageUrl;

    @NotNull
    private String badgeDescription;

    private Long badgePrice;
    private boolean isSelling;
    private LocalDate badgeEndDate;

    @Builder.Default
    private Long badgeSoldNumber = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "badge", cascade = CascadeType.ALL)
    private List<UserBadge> userBadges = new ArrayList<>();

    public void modifiyBadgeWithDto(BadgeRequest badge) {
        this.badgeName = badge.getName();
        this.badgeImageUrl = badge.getImageUrl();
        this.badgeDescription = badge.getDescription();
        this.badgePrice = badge.getPrice();
        this.isSelling = badge.isSelling();
        this.badgeEndDate = badge.getBadgeEndDate();
    }
}
