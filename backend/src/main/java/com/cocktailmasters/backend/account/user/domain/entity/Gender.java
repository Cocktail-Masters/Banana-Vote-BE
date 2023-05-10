package com.cocktailmasters.backend.account.user.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Gender {

    MALE("남성", Arrays.asList("M", "m", "Male", "male")),
    FEMALE("여성", Arrays.asList("F", "f", "Female", "female"));

    private final String key;
    private final List<String> genderNameList;

    public static Gender findByGenderName(String genderName) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.hasGenderName(genderName))
                .findAny()
                .orElse(null);
    }

    public boolean hasGenderName(String genderName) {
        return genderNameList.stream()
                .anyMatch(name -> name.equals(genderName));
    }
}