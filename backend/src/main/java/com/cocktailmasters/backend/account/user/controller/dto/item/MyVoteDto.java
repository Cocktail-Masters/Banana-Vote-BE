package com.cocktailmasters.backend.account.user.controller.dto.item;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MyVoteDto {

    private long id;
    private String title;
    private boolean isClosed;
}
