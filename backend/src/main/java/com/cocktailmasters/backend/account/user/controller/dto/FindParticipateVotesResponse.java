package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.controller.dto.item.VoteDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindParticipateVotesResponse {

    private List<VoteDto> votes;
}
