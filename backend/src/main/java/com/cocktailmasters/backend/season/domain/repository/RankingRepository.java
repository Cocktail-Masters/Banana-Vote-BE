package com.cocktailmasters.backend.season.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;

public interface RankingRepository extends JpaRepository<SeasonRanking, Long> {
    long countBySeasonId(long seasonId);

    @Query("SELECT score FROM SeasonRanking r WHERE r.season.id = :seasonId AND r.user.id = :userId")
    long findScoreBySeasonIdAndUser(@Param("seasonId") long seasonId, @Param("userId") long userId);

    Page<SeasonRanking> findBySeasonIdOrderByScoreDesc(long seasonId, Pageable pageable);

    @Query("SELECT COUNT(r) FROM SeasonRanking r WHERE r.season.id = :seasonId AND r.score > :score")
    long countUserRankingByScore(@Param("seasonId") long seasonId, @Param("score") long score);

    Optional<SeasonRanking> findBySeasonIdAndUserId(long seasonId, long userId);
}
