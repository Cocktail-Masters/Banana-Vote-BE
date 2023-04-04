package com.cocktailmasters.backend.common.contorller.dto;

import com.cocktailmasters.backend.common.domain.entity.Tag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CreateTagsRequest {

    private List<String> tags;

    public List<Tag> toTagEntity() {
        return tags.stream().map(tag -> Tag.builder()
                        .tagName(tag)
                        .build())
                .collect(Collectors.toList());
    }
}
