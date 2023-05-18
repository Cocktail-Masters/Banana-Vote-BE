package com.cocktailmasters.backend.goods.controller.dto;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.UserGoods;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserGoodsResponse {

    private long id;
    private String name;
    private String description;

    @JsonProperty("image_url")
    private String imageUrl;

    private GoodsType type;
    private long ea;

    @JsonProperty("expiration_date")
    private String expirationDate;

    @JsonProperty("is_using")
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
