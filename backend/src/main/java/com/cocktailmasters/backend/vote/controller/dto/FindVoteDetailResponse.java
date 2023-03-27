package com.cocktailmasters.backend.vote.controller.dto;

import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemsDto;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteDto;
import com.cocktailmasters.backend.vote.controller.dto.item.WriterDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindVoteDetailResponse {

    private VoteDto vote;
    private WriterDto writer;
    private List<VoteItemsDto> voteItems;
}
