package com.cocktailmasters.backend.account.user.controller.dto;

import com.cocktailmasters.backend.account.user.controller.dto.item.TagDto;
import lombok.Getter;

@Getter
public class DeleteInterestTagRequest {

    private TagDto tag;
}
