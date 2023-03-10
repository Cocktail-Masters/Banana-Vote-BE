package com.cocktailmasters.backend.goods.domain.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "user_goods_id"))
public class UserGoods {

    @Builder.Default
    private int goods_number = 0;

    private LocalDateTime goods_expiration_date;
}
