package com.cocktailmasters.backend.picket.controller.dto;

import java.util.List;

import com.cocktailmasters.backend.picket.controller.dto.item.PicketItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PicketResponse {
    
    private String last_updated;

    private List<PicketItem> pickets;
}
