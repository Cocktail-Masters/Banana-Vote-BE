package com.cocktailmasters.backend.vote.controller.dto;

import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemDto;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteDetailDto;
import com.cocktailmasters.backend.vote.controller.dto.item.WriterDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindVoteDetailResponse {

    private VoteDetailDto vote;
    private WriterDto writer;
    private List<VoteItemDto> voteItems;
}
