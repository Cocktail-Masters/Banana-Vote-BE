package com.cocktailmasters.backend.goods.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.goods.controller.dto.item.BadgeItemDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BadgeResponse {
    @JsonProperty("total_page")
    private long totalPage;

    @JsonProperty("badge_list")
    List<BadgeItemDto> badgeList;
}
