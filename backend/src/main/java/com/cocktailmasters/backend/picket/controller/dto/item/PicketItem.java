package com.cocktailmasters.backend.picket.controller.dto.item;

import com.cocktailmasters.backend.picket.domain.entity.Picket;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PicketItem {

    private long id;

    @JsonProperty("vote_id")
    private long voteId;

    @JsonProperty("owner_id")
    private long ownerId;

    @JsonProperty("picket_image_url")
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
