package com.cocktailmasters.backend.goods.controller.dto.item;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.Goods;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoodsItemDto {
    
    private long id;
    private String name;
    private String description;
    private GoodsType type;
    private long price;

    @JsonProperty("image_url")
    private String imageUrl;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("sell_count")
    private long sellCount;
    
    @JsonProperty("use_period")
    private long validPeriod;

    @JsonProperty("remaining_quantity")
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
                .validPeriod(goods.getGoodsValidityPeriod())
                .remainingQuantity(goods.getGoodsRemainingQuantity())
                .build();
    }
}
