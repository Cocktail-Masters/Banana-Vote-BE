package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriterDto {

    private Long id;
    private String nickname;
    private String badgeImageUrl;
}
