package com.cocktailmasters.backend.vote.controller.dto;

import com.cocktailmasters.backend.vote.controller.dto.item.OpinionsDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindVoteOpinionsResponse {

    List<OpinionsDto> opinions;
}
