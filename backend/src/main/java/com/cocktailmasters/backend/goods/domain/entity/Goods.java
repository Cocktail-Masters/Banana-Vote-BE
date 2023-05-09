package com.cocktailmasters.backend.goods.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.goods.domain.GoodsType;

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
@AttributeOverride(name = "id", column = @Column(name = "goods_id"))
@Entity
public class Goods extends BaseEntity {

    @NotNull
    private String goodsName;

    @NotNull
    private String goodsDescription;

    @Enumerated(EnumType.STRING)
    @NotNull
    private GoodsType goodsType;

    private String goodsImageUrl;

    @Builder.Default
    private Long goodsValidityPeriod = 30L;

    @NotNull
    private Long goodsPrice;

    @NotNull
    @Builder.Default
    private Long goodsRemainingQuantity = -1L;

    @Builder.Default
    private LocalDate saleStartDate = LocalDate.now();
    @Builder.Default
    private LocalDate saleEndDate = LocalDate.of(2100, 12, 31);

    @Builder.Default
    private Long goodsSoldNumber = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    private List<UserGoods> userGoods = new ArrayList<>();

    public void soldGoods(long quantity) {
        goodsRemainingQuantity -= quantity;
        goodsSoldNumber += quantity;
    }
}
