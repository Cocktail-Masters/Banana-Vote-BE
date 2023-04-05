package com.cocktailmasters.backend.common.contorller.dto.item;

import com.cocktailmasters.backend.common.domain.entity.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagDto {

    private Long id;
    private String name;
    private Long usedNumber;

    public static TagDto createTagDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getTagName())
                .usedNumber(tag.getTagUsedNumber())
                .build();
    }
}
