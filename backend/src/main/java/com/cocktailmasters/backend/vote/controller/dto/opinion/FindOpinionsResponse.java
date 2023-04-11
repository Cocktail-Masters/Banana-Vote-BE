package com.cocktailmasters.backend.vote.controller.dto.opinion;

import com.cocktailmasters.backend.vote.controller.dto.item.OpinionDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindOpinionsResponse {

    List<OpinionDto> opinions;
    List<Long> bestIds;
    int opinionNumber;
}
