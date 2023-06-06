package com.cocktailmasters.backend.goods.controller.dto;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.goods.domain.GoodsType;
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

    private String name;

    private String description;

    private String imageUrl;

    private GoodsType type;

    private long ea;

    private String expirationDate;

    private boolean isUsing;

    public static UserGoodsResponse createUserGoodsReponse(UserGoods userGoods) {
        return UserGoodsResponse.builder()
                .id(userGoods.getId())
                .name(userGoods.getGoods().getGoodsName())
                .description(userGoods.getGoods().getGoodsDescription())
                .imageUrl(userGoods.getGoods().getGoodsImageUrl())
                .type(userGoods.getGoods().getGoodsType())
                .ea(userGoods.getGoodsAmount())
                .expirationDate(userGoods.getGoodsExpirationDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .isUsing(userGoods.isUsing())
                .build();
    }
}
