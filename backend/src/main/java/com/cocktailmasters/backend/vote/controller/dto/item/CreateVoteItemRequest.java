package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateVoteItemRequest {

    private int itemNumber;
    private String title;
    private String imageUrl;
    private String iframeLink;
}
