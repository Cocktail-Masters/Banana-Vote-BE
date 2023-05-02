package com.cocktailmasters.backend.vote.controller.dto.opinion;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateOpinionRequest {

    @NotNull
    private Long voteId;

    @NotNull
    private String content;

    public Opinion toOpinionEntity(User writer, Vote vote) {
        return Opinion.builder()
                .user(writer)
                .vote(vote)
                .opinionContent(content)
                .build();
    }
}
