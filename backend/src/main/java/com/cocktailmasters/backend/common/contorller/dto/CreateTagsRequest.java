package com.cocktailmasters.backend.common.contorller.dto;

import com.cocktailmasters.backend.common.contorller.dto.item.TagsDto;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagsRequest {

    private TagsDto tagsDto;

    public List<Tag> toTagEntity() {
        return tagsDto.getTags()
                .stream()
                .map(tag -> Tag.builder()
                        .tagName(tag)
                        .build())
                .collect(Collectors.toList());
    }
}
