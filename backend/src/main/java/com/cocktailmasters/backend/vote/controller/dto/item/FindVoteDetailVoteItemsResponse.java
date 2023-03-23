package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindVoteDetailVoteItemsResponse {

    private String title;
    private String iframeLink;
    private String itemNumber;
    private String ImageUrl;
    private Long totalPoints;
    private int votedNumber;
}
