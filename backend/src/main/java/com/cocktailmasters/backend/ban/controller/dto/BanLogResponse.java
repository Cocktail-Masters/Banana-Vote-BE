package com.cocktailmasters.backend.ban.controller.dto;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.ban.domain.entity.BanLog;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BanLogResponse {
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("ban_reason")
    private String banReason;

    @JsonProperty("ban_count")
    private long banCount;

    public BanLogResponse createLogReponse(BanLog banLog) {
        User bannedUser = banLog.getUser();

        return BanLogResponse.builder()
                .userId(bannedUser.getId())
                .userName(bannedUser.getNickname())
                .banReason(banLog.getBanReason())
                .banCount(bannedUser.getBanLogs().size())
                .build();
    }
}
