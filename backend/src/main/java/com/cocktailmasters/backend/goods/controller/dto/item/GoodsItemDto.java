package com.cocktailmasters.backend.goods.controller.dto.item;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.Goods;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoodsItemDto {

    private long id;
    private String name;
    private String description;
    private GoodsType type;
    private long price;

    private String imageUrl;

    private String startDate;

    private String endDate;

    private long sellCount;

    private long validPeriod;

    private long remainingQuantity;

    public static GoodsItemDto createGoodItemDto(Goods goods) {
        return GoodsItemDto.builder()
                .id(goods.getId())
                .name(goods.getGoodsName())
                .description(goods.getGoodsDescription())
                .type(goods.getGoodsType())
                .price(goods.getGoodsPrice())
                .imageUrl(goods.getGoodsImageUrl())
                .startDate(goods.getSaleStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .endDate(goods.getSaleEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .sellCount(goods.getGoodsSoldNumber())
                .validPeriod(goods.getGoodsUsingPeriod())
                .remainingQuantity(goods.getGoodsRemainingQuantity())
                .build();
    }
}
