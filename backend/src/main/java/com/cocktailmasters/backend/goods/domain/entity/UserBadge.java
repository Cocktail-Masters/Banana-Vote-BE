package com.cocktailmasters.backend.goods.domain.entity;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "user_badge_id"))
@Entity
public class UserBadge extends BaseEntity {

    @Builder.Default
    private boolean isEquipped = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Badge badge;

    public void equipBadge() {
        this.isEquipped = true;
    }

    public void unequipBadge() {
        this.isEquipped = false;
    }
}
