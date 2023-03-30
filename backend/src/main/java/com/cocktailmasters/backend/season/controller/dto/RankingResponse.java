package com.cocktailmasters.backend.season.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.season.controller.dto.item.UserRanking;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RankingResponse {
    @JsonProperty("total_page")
    private long totalPage;

    @JsonProperty("ranking_list")
    List<UserRanking> rankingList;
}
