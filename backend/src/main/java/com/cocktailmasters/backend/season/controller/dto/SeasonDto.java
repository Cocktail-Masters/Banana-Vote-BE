package com.cocktailmasters.backend.season.controller.dto;

import java.time.LocalDate;

import com.cocktailmasters.backend.season.domain.entity.Season;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDto {
    private long id = -1;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("start_date")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("end_date")
    private LocalDate endDate;

    private String description;

    public SeasonDto(Season season) {
        this.id = season.getId();
        this.startDate = season.getSeasonStartDate();
        this.endDate = season.getSeasonEndDate();
        this.description = season.getSeasonDescription();
    }
}
