package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemCreateDto;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.cocktailmasters.backend.vote.domain.entity.VoteTag;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateVoteRequest {

    private String voteTitle;
    private String voteContent;
    private LocalDateTime voteEndDate;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private List<VoteItemCreateDto> voteItems;
    private List<String> tags;

    public Vote toVoteEntity(User user,
                             List<VoteItem> voteItems,
                             List<VoteTag> voteTags) {
        return Vote.builder()
                .user(user)
                .voteTitle(voteTitle)
                .voteContent(voteContent)
                .voteEndDate(voteEndDate)
                .isAnonymous(isAnonymous)
                .isPublic(isPublic)
                .voteItems(voteItems)
                .voteTags(voteTags)
                .build();
    }
}
