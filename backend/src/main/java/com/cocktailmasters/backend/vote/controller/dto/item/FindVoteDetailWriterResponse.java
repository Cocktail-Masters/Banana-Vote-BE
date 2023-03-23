package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FindVoteDetailWriterResponse {

    private Long id;
    private String nickname;
    private String badgeImageUrl;
}
