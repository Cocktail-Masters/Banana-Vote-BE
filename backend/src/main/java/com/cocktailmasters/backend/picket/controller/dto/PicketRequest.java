package com.cocktailmasters.backend.picket.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PicketRequest {
    
    private int position = -1;

    @JsonProperty("picket_image_url")
    private String picketImageUrl = "";

    @JsonProperty("current_price")
    private long curPrice = -1;

    @JsonProperty("paid_price")
    private long paidPrice = -1;
}
