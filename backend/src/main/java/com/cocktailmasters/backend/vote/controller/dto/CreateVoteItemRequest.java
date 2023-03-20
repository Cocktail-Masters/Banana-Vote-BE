package com.cocktailmasters.backend.vote.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateVoteItemRequest {

    private String title;
    private String imageUrl;
    private String iframeLink;
}
