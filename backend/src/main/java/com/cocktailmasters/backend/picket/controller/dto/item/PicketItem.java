package com.cocktailmasters.backend.picket.controller.dto.item;

import com.cocktailmasters.backend.picket.domain.entity.Picket;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PicketItem {

    private long id;

    private long voteId;

    private long ownerId;

    private String picketImageUrl;

    private int position;

    private long price;

    public static PicketItem createPicketItem(Picket picket) {
        return PicketItem.builder()
                .id(picket.getId())
                .ownerId(picket.getUser().getId())
                .voteId(picket.getVote().getId())
                .picketImageUrl(picket.getPicketImageUrl())
                .position(picket.getPosition())
                .price(picket.getPrice())
                .build();
    }
}
