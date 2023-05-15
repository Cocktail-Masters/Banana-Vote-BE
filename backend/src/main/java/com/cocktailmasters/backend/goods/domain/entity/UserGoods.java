package com.cocktailmasters.backend.goods.domain.entity;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "user_goods_id"))
@Entity
public class UserGoods extends BaseEntity {

    @Builder.Default
    private int goodsAmount = 0;

    private LocalDate goodsExpirationDate;

    private boolean isUsing;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Goods goods;

    public void addQuantity(int quanity) {
        goodsAmount += quanity;
    }
}
