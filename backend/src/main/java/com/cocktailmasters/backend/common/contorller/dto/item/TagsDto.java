package com.cocktailmasters.backend.common.contorller.dto.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagsDto {

    private List<String> tags;
}
