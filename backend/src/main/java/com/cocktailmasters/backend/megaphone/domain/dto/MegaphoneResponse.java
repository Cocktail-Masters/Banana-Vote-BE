package com.cocktailmasters.backend.megaphone.domain.dto;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.megaphone.domain.dto.item.UserInfoItem;
import com.cocktailmasters.backend.megaphone.domain.entity.Megaphone;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MegaphoneResponse {
    private long id;

    private String endDateTime;

    private String content;

    private String voteLink;

    private UserInfoItem user;

    public static MegaphoneResponse createMegaphoneResponse(Megaphone megaphone) {
        return MegaphoneResponse.builder()
                .id(megaphone.getId())
                .endDateTime(megaphone.getMegaphoneEndDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .content(megaphone.getMegaphoneContent())
                .voteLink(megaphone.getVoteLink())
                .user(new UserInfoItem(megaphone.getUser()))
                .build();
    }
}
