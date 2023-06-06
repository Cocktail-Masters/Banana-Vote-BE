package com.cocktailmasters.backend.season.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.season.controller.dto.item.UserRanking;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RankingResponse {

    private long totalPage;

    private long nowPage;

    List<UserRanking> rankingList;
}
