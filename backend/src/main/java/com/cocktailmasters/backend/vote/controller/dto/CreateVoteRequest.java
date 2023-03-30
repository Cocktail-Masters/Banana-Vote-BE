package com.cocktailmasters.backend.vote.controller.dto;

import com.cocktailmasters.backend.vote.controller.dto.item.CreateVoteItemRequest;
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
    private List<CreateVoteItemRequest> voteItems;
    private List<String> tags;
}
