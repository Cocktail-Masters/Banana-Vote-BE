package com.cocktailmasters.backend.season.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;

public interface RankingRepository extends PagingAndSortingRepository<SeasonRanking, Long> {
    long countBySeasonId(long seasonId);

    Optional<Long> findScoreBySeasonIdAndUser(long seasonId, User user);

    Page<SeasonRanking> findBySeasonIdOrderByScoreDesc(long seasonId, Pageable pageable);

    @Query(value = "SELECT COUNT(*) + 1 FROM season_ranking WHERE season_id = :seasonId AND score > :score", nativeQuery = true)
    long getUserRankingByScore(long seasonId, long score);
}
