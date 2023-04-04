package com.cocktailmasters.backend.common.contorller.dto;

import com.cocktailmasters.backend.common.domain.entity.Tag;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CreateTagsRequest {

    private List<String> tags;

    public Tag toTagEntity(String tag) {
        return Tag.builder()
                .tagName(tag)
                .build();
    }
}
