package com.cocktailmasters.backend.vote.controller.dto;

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
