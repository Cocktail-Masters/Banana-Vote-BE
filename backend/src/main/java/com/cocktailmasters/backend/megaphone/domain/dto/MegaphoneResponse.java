package com.cocktailmasters.backend.megaphone.domain.dto;

import java.time.format.DateTimeFormatter;

import com.cocktailmasters.backend.megaphone.domain.dto.item.UserInfoItem;
import com.cocktailmasters.backend.megaphone.domain.entity.Megaphone;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MegaphoneResponse {
    private long id;

    @JsonProperty("end_date_time")
    private String endDateTime;

    private String content;

    @JsonProperty("vote_link")
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
