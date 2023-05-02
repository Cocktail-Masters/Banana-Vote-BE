package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VoteItemDto {

    private int itemNumber;
    private String title;
    private String iframeLink;
    private String imageUrl;
    private Long totalPoints;
    private int votedNumber;

    public static VoteItemDto createVoteItemDto(VoteItem voteItem) {
        return VoteItemDto.builder()
                .itemNumber(voteItem.getVoteItemNumber())
                .title(voteItem.getVoteItemTitle())
                .iframeLink(voteItem.getIframeLink())
                .imageUrl(voteItem.getVoteItemImageUrl())
                .totalPoints(voteItem.getTotalPoints())
                .votedNumber(voteItem.getVotedNumber())
                .build();
    }
}
