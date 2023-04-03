package com.cocktailmasters.backend.vote.controller.dto.item;

import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PredictionItemDto {

    private Long voteItemId;
    private int voteItemNumber;
    private Long totalPoint;
    private Long bestPoint;

    public static PredictionItemDto createPredictionItemDto(VoteItem voteItem){
        return PredictionItemDto.builder()
                .voteItemId(voteItem.getId())
                .voteItemNumber(voteItem.getVoteItemNumber())
                .totalPoint(voteItem.getTotalPoints())
                .bestPoint(voteItem.getBestPoints())
                .build();
    }
}
