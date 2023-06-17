package com.cocktailmasters.backend.season.domain.repository;

import com.cocktailmasters.backend.season.domain.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
