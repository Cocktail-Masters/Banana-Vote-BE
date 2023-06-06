package com.cocktailmasters.backend.goods.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.goods.controller.dto.item.GoodsItemDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoodsResponse {

    private long totalPage;

    List<GoodsItemDto> goodsList;
}
