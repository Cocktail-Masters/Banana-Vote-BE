package com.cocktailmasters.backend.season.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.season.domain.entity.Season;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
