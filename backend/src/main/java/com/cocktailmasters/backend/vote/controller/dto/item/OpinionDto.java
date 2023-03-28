package com.cocktailmasters.backend.vote.controller.dto.item;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OpinionDto {

    private Long id;
    private WriterDto writer;
    private String content;
    private int agreedNumber;
    private int disagreedNumber;
    private LocalDateTime createdDate;
}
