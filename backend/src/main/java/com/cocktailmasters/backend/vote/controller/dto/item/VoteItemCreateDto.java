package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VoteItemCreateDto {

    private int itemNumber;
    private String title;
    private String imageUrl;
    private String iframeLink;

    public static VoteItem toVoteItemEntity(VoteItemCreateDto createVoteItemRequest) {
        return VoteItem.builder()
                .voteItemNumber(createVoteItemRequest.getItemNumber())
                .voteItemTitle(createVoteItemRequest.getTitle())
                .voteItemImageUrl(createVoteItemRequest.getImageUrl())
                .iframeLink(createVoteItemRequest.getIframeLink())
                .build();
    }
}
