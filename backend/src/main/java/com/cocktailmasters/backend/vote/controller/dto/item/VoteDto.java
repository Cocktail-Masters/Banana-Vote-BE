package com.cocktailmasters.backend.vote.controller.dto.item;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VoteDto {

    private VoteDetailDto vote;
    private WriterDto writer;
    private List<VoteItemDto> voteItems;
    private OpinionDto bestOpinion;
}
