package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SimpleVoteDto {

    private Long id;
    private String title;
    private Long hits;
    private int voted_number;

    public static SimpleVoteDto createSimpleVoteDto(Vote vote) {
        return SimpleVoteDto.builder().
                id(vote.getId())
                .title(vote.getVoteTitle())
                .hits(vote.getVoteHits())
                .voted_number(vote.getVotedNumber())
                .build();
    }
}
