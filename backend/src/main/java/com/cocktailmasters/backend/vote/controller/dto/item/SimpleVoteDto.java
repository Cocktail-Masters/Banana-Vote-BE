package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SimpleVoteDto {

    private Long id;
    private String title;
    private Long hits;
    private int voted_number;
}
