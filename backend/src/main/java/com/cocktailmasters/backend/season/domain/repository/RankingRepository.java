package com.cocktailmasters.backend.season.domain.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;

public interface RankingRepository extends JpaRepository<SeasonRanking, Long> {
    List<SeasonRanking> findBySeasonId(long seasonId, Pageable pageable);
}
