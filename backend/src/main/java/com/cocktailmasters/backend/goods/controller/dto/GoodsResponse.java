package com.cocktailmasters.backend.goods.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.goods.controller.dto.item.GoodsItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsResponse {
    @JsonProperty("total_page")
    private long totalPage;

    @JsonProperty("goods_list")
    List<GoodsItemDto> goodsList;
}
