package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class FindVoteDetailVoteResponse {

    private int id;
    private String title;
    private String imageUrl;
    private String content;
    private Boolean isEvent;
    private Boolean isAnonymous;
    private Boolean isPublic;
    private Boolean isClosed;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long hits;
    private int votedNumber;
    private int opinionNumber;
    private List<String> tags;
}
