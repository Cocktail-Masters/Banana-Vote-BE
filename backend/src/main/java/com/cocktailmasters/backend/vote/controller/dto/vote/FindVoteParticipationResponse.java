package com.cocktailmasters.backend.vote.controller.dto.vote;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindVoteParticipationResponse {

    private boolean isParticipation;
    private Long voteItemId;
    private int voteNumber;
    private Long point;
}
