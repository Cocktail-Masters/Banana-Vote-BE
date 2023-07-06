package com.cocktailmasters.backend.ban.controller.dto;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.ban.domain.entity.BanLog;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BanLogResponse {

    private String userName;

    private long userId;

    private String banReason;

    private long banCount;

    public static BanLogResponse createLogReponse(BanLog banLog) {
        User bannedUser = banLog.getUser();

        return BanLogResponse.builder()
                .userId(bannedUser.getId())
                .userName(bannedUser.getNickname())
                .banReason(banLog.getBanReason())
                .banCount(bannedUser.getBanLogs().size())
                .build();
    }
}
