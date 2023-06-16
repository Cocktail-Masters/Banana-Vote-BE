package com.cocktailmasters.backend.goods.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.goods.controller.dto.GoodsRequest;
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
    private Long goodsUsingPeriod = 30L;

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

    /**
     * modify goods with DTO
     * 
     * @param goodsRequest
     * @return numbed of modified field
     */
    public void modifiyGoodsInfo(GoodsRequest goodsRequest) {
        this.goodsName = goodsRequest.getName();
        this.goodsDescription = goodsRequest.getDescription();
        this.goodsImageUrl = goodsRequest.getImageUrl();
        this.goodsPrice = goodsRequest.getPrice();
        this.goodsUsingPeriod = goodsRequest.getValidPeriod();
        this.goodsRemainingQuantity = goodsRequest.getRemainingQuantity();
        this.goodsType = goodsRequest.getType();
        this.saleStartDate = goodsRequest.getStartDate();
        this.saleEndDate = goodsRequest.getEndDate();
    }
}
