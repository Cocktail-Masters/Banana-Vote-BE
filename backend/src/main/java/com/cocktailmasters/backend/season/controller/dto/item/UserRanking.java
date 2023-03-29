package com.cocktailmasters.backend.season.controller.dto.item;

import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRanking {
    private long ranking = -1;

    private String nickname;

    private long score;

    public UserRanking(SeasonRanking seasonRanking, long ranking) {
        this.nickname = seasonRanking.getUser().getNickname();
        this.score = seasonRanking.getScore();
        this.ranking = ranking;
    }
}
