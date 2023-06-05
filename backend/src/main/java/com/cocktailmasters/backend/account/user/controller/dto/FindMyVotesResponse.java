package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.controller.dto.item.MyVoteDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyVotesResponse {

    private List<MyVoteDto> votes;
}
