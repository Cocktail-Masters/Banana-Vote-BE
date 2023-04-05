package com.cocktailmasters.backend.common.contorller.dto;

import com.cocktailmasters.backend.common.contorller.dto.item.TagDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindTop10TagsResponse {

    private List<TagDto> tags;
}
