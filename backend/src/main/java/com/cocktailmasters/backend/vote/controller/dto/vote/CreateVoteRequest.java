package com.cocktailmasters.backend.vote.controller.dto.vote;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.vote.controller.dto.item.VoteItemCreateDto;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.cocktailmasters.backend.vote.domain.entity.VoteTag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CreateVoteRequest {

    private String voteTitle;
    private String voteContent;
    private String voteImageUrl;
    private String voteThumbnailUrl;
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
                .voteImageUrl(voteImageUrl)
                .voteThumbnailUrl(voteThumbnailUrl)
                .voteEndDate(voteEndDate)
                .isAnonymous(isAnonymous)
                .isPublic(isPublic)
                .voteItems(voteItems)
                .voteTags(voteTags)
                .build();
    }
}
