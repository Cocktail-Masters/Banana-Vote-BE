package com.cocktailmasters.backend.goods.controller.dto;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.Goods;
import com.cocktailmasters.backend.goods.domain.entity.UserGoods;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserGoodsResponse {

    private long id;

    private Long goodId;

    private String name;

    private String description;

    private String imageUrl;

    private GoodsType type;

    private long ea;

    private String expirationDate;

    private boolean isUsing;

    public static UserGoodsResponse createUserGoodsReponse(UserGoods userGoods) {
        Goods goodsInfo = userGoods.getGoods();

        return UserGoodsResponse.builder()
                .id(userGoods.getId())
                .goodId(goodsInfo.getId())
                .name(goodsInfo.getGoodsName())
                .description(goodsInfo.getGoodsDescription())
                .imageUrl(goodsInfo.getGoodsImageUrl())
                .type(goodsInfo.getGoodsType())
                .ea(userGoods.getGoodsAmount())
                .expirationDate(userGoods.getGoodsExpirationDate() == null
                        ? ""
                        : userGoods.getGoodsExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .isUsing(userGoods.isUsing())
                .build();
    }
}
