package com.cocktailmasters.backend.vote.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum SortBy {

    LATEST(1, "created_date", "최신순"),
    POPULARITY(2, "voted_number", "참여순"),
    HITS(3, "vote_hits", "조회순"),
    OPINION(4, "opinion_number", "댓글순");

    private final int number;
    private final String value;
    private final String title;

    private static final Map<Integer, SortBy> BY_NUMBER = new HashMap<>();

    static {
        for (SortBy e : values()) {
            BY_NUMBER.put(e.number, e);
        }
    }

    public static String valueOfNumber(int number) {
        return BY_NUMBER.get(number).getValue();
    }
}
