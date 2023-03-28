package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteItemsDto {

    private int itemNumber;
    private String title;
    private String iframeLink;
    private String imageUrl;
    private Long totalPoints;
    private int votedNumber;
}
