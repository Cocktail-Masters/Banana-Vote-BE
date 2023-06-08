package com.cocktailmasters.backend.season.controller.dto.item;

import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserRanking {

    private long userId;

    private long ranking = -1;

    private String nickname;

    private long score;

    public UserRanking(SeasonRanking seasonRanking, long ranking) {
        this.userId = seasonRanking.getUser().getId();
        this.nickname = seasonRanking.getUser().getNickname();
        this.score = seasonRanking.getScore();
        this.ranking = ranking;
    }
}
