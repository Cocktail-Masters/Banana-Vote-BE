package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.controller.dto.item.OpinionDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FindMyOpinionsResponse {

    private List<OpinionDto> opinions;
}
