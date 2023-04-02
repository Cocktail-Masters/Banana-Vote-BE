package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateVoteItemRequest {

    private int itemNumber;
    private String title;
    private String imageUrl;
    private String iframeLink;

    public static VoteItem toVoteItemEntity(CreateVoteItemRequest createVoteItemRequest) {
        return VoteItem.builder()
                .voteItemNumber(createVoteItemRequest.getItemNumber())
                .voteItemTitle(createVoteItemRequest.getTitle())
                .voteItemImageUrl(createVoteItemRequest.getImageUrl())
                .iframeLink(createVoteItemRequest.getIframeLink())
                .build();
    }
}
