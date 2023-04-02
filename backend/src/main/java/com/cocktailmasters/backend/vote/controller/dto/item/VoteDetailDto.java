package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.Vote;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class VoteDetailDto {

    private Long id;
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

    public static VoteDetailDto createVoteDetailDto(Vote vote) {
        return VoteDetailDto.builder()
                .id(vote.getId())
                .title(vote.getVoteTitle())
                .imageUrl(vote.getVoteImageUrl())
                .content(vote.getVoteContent())
                .isEvent(vote.isEvent())
                .isAnonymous(vote.isAnonymous())
                .isPublic(vote.isPublic())
                .isClosed(vote.isClosed())
                .startDate(vote.getCreatedDate())
                .endDate(vote.getVoteEndDate())
                .hits(vote.getVoteHits())
                .votedNumber(vote.getVotedNumber())
                .opinionNumber(vote.getOpinionNumber())
                .tags(vote.getVoteTags()
                        .stream()
                        .map(tag -> tag.getTag().getTagName())
                        .collect(Collectors.toList()))
                .build();
    }
}
