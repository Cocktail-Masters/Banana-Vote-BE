package com.cocktailmasters.backend.common.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportedReasonType {

    ADVERTISEMENT("광고"),
    ADULT("음란물"),
    VIOLENCE("비난"),
    ETC("기타");

    private final String type;
}
