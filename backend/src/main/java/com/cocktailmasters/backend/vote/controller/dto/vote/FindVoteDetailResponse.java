package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.cocktailmasters.backend.vote.controller.dto.item.VoteDetailDto;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemDto;
import com.cocktailmasters.backend.vote.controller.dto.item.WriterDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindVoteDetailResponse {

    private VoteDetailDto vote;
    private WriterDto writer;
    private List<VoteItemDto> voteItems;
}
