package com.cocktailmasters.backend.ban.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BanRequest {
    @JsonProperty("ban_reason")
    private String banReason;
}
