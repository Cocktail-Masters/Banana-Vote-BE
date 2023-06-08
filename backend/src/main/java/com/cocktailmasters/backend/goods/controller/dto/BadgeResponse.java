package com.cocktailmasters.backend.goods.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.goods.controller.dto.item.BadgeItemDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BadgeResponse {

    private long totalPage;

    List<BadgeItemDto> badgeList;
}
