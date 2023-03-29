package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.Vote;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VoteDto {

    private Vote vote;
    private WriterDto writer;
    private List<VoteItemDto> voteItems;
    private OpinionDto bestOpinion;
}
