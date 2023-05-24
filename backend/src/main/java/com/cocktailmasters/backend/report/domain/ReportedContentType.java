package com.cocktailmasters.backend.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportedContentType {

    VOTE("투표"),
    OPINION("의견"),
    PICKET("피켓"),
    Megaphone("확성기");

    private final String type;
}
