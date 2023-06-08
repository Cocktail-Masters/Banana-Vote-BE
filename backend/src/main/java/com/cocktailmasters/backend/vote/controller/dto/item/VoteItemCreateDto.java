package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoteItemCreateDto {

    private int itemNumber;
    private String title;
    private String imageUrl;
    private String iframeLink;

    public static VoteItem toVoteItemEntity(VoteItemCreateDto createVoteItemRequest, Vote vote) {
        return VoteItem.builder()
                .voteItemNumber(createVoteItemRequest.getItemNumber())
                .voteItemTitle(createVoteItemRequest.getTitle())
                .voteItemImageUrl(createVoteItemRequest.getImageUrl())
                .iframeLink(createVoteItemRequest.getIframeLink())
                .vote(vote)
                .build();
    }
}
