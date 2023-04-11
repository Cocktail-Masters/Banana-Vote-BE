package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OpinionDto {

    private Long id;
    private WriterDto writer;
    private Boolean isAgree;
    private String content;
    private int agreedNumber;
    private int disagreedNumber;
    private LocalDateTime createdDate;

    public static OpinionDto createOpinionDto(Opinion opinion, Boolean isAgree) {
        return OpinionDto.builder()
                .id(opinion.getId())
                .writer(WriterDto.builder()
                        .id(opinion.getUser().getId())
                        .nickname(opinion.getUser().getNickname())
                        .badgeImageUrl(opinion.getUser().getEquippedBadgeImageUrl())
                        .build())
                .isAgree(isAgree)
                .content(opinion.getOpinionContent())
                .agreedNumber(opinion.getAgreedNumber())
                .disagreedNumber(opinion.getDisagreedNumber())
                .createdDate(opinion.getCreatedDate())
                .build();
    }
}
