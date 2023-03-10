package com.cocktailmasters.backend.goods.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "goods_id"))
public class Goods extends BaseEntity {

    @NotNull
    private String goodsName;

    @NotNull
    private String goodsDescription;

    @NotNull
    private Long goodsPrice;

    private LocalDateTime sale_start_date;
    private LocalDateTime sale_end_date;

    @Builder.Default
    private Long goodsSoldNumber = 0L;
}
