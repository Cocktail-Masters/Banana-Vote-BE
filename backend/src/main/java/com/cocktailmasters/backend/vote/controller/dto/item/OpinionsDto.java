package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OpinionsDto {

    private Long id;
    private Long writerId;
    private String content;
    private int agreedNumber;
    private int disagreedNumber;
}
