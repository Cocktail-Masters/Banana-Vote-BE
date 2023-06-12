package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemCreateDto;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateVoteRequest {

    @NotNull
    private String voteTitle;

    private String voteContent;

    @NotNull
    private LocalDateTime voteEndDate;

    @NotNull
    private Boolean isPublic;

    @NotNull
    private Boolean isEvent;

    @NotNull
    private Boolean isAnonymous;

    @NotNull
    private List<VoteItemCreateDto> voteItems;

    private List<String> tags;

    public Vote toVoteEntity(User user) {
        return Vote.builder()
                .user(user)
                .voteTitle(voteTitle)
                .voteContent(voteContent)
                .voteEndDate(voteEndDate)
                .isAnonymous(isAnonymous)
                .isPublic(isPublic)
                .isEvent(isEvent)
                .build();
    }
}
