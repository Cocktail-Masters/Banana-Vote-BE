package com.cocktailmasters.backend.season.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
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
    private LocalDateTime seasonStartDate;

    @NotNull
    private LocalDateTime seasonEndDate;

    private String seasonDescription;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<SeasonRanking> seasonRankings = new ArrayList<>();
}
