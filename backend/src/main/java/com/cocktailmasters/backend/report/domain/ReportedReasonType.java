package com.cocktailmasters.backend.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportedReasonType {

    ADVERTISEMENT("광고"),
    ADULT("음란물"),
    VIOLENCE("혐오"),
    ETC("기타");

    private final String type;
}
