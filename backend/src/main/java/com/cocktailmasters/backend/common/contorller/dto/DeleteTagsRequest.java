package com.cocktailmasters.backend.common.contorller.dto;

import com.cocktailmasters.backend.common.contorller.dto.item.TagsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteTagsRequest {

    private TagsDto tagsDto;
}
