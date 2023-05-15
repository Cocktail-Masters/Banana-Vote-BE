package com.cocktailmasters.backend.megaphone.domain.dto.item;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class UserInfoItem {

    @JsonProperty("user_id")
    private long userId;

    private String nickname;

    public UserInfoItem(User user) {
        this.userId = user.getId();
        this.nickname = user.getNickname();
    }
}
