package com.cocktailmasters.backend.season.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "season_id"))
@Entity
public class Season extends BaseEntity {

    @NotNull
    private LocalDate seasonStartDate;

    @NotNull
    private LocalDate seasonEndDate;

    private String seasonDescription;

    @Builder.Default
    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<SeasonRanking> seasonRankings = new ArrayList<>();
}
