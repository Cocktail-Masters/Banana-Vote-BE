package com.cocktailmasters.backend.vote.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum OpinionSortBy {

    AGREE(1, "agreed_number", "공감순"),
    LATEST(2, "created_date", "최신순");

    private final int number;
    private final String value;
    private final String title;

    private static final Map<Integer, OpinionSortBy> BY_NUMBER = new HashMap<>();

    static {
        for (OpinionSortBy e : values()) {
            BY_NUMBER.put(e.number, e);
        }
    }

    public static String valueOfNumber(int number) {
        return BY_NUMBER.get(number).getValue();
    }
}
