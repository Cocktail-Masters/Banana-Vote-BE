package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PredictionItemDto {

    private Long voteItemId;
    private int voteItemNumber;
    private Long totalPoint;
    private Long bestPoint;

    public static PredictionItemDto createPredictionItemDto(VoteItem voteItem) {
        return PredictionItemDto.builder()
                .voteItemId(voteItem.getId())
                .voteItemNumber(voteItem.getVoteItemNumber())
                .totalPoint(voteItem.getTotalPoints())
                .bestPoint(voteItem.getBestPoints())
                .build();
    }
}
