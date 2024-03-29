package com.cocktailmasters.backend.account.user.controller.dto.item;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OpinionDto {

    private long id;
    private String content;
    private int nAgree;
    private int nDisAgree;
    private LocalDateTime createdDate;
}
