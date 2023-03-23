package com.cocktailmasters.backend.vote.controller.dto;

import com.cocktailmasters.backend.vote.controller.dto.item.FindVoteDetailVoteItemsResponse;
import com.cocktailmasters.backend.vote.controller.dto.item.FindVoteDetailVoteResponse;
import com.cocktailmasters.backend.vote.controller.dto.item.FindVoteDetailWriterResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindVoteDetailResponse {

    private FindVoteDetailVoteResponse vote;
    private FindVoteDetailWriterResponse writer;
    private List<FindVoteDetailVoteItemsResponse> voteItems;
}
