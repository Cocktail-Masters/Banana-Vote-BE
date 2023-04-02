package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VoteDto {

    private VoteDetailDto vote;
    private WriterDto writer;
    private List<VoteItemDto> voteItems;
    private OpinionDto bestOpinion;
}
