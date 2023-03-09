package com.cocktailmasters.backend.goods.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "user_badge_id"))
public class UserBadge {

    @Builder.Default
    private boolean isEquipped = false;
}
