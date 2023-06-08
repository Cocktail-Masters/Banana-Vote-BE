package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindVoteParticipationResponse {

    private boolean isParticipation;
    private Long voteItemId;
    private int voteNumber;
    private Long point;
}
