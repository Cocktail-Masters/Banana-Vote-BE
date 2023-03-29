package com.cocktailmasters.backend.season.controller.dto.item;

import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRanking {
    @JsonProperty("user_id")
    private long userId;

    private String nickname;

    private long score;

    public UserRanking(SeasonRanking seasonRanking) {
        this.userId = seasonRanking.getId();
        this.nickname = seasonRanking.getUser().getNickname();
        this.score = seasonRanking.getScore();
    }
}
