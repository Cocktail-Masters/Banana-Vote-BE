package com.cocktailmasters.backend.season.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.season.controller.dto.RankingResponse;
import com.cocktailmasters.backend.season.controller.dto.item.UserRanking;
import com.cocktailmasters.backend.season.domain.entity.Season;
import com.cocktailmasters.backend.season.domain.entity.SeasonRanking;
import com.cocktailmasters.backend.season.domain.repository.RankingRepository;
import com.cocktailmasters.backend.season.domain.repository.SeasonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RankingService {
    
    private final RankingRepository rankingRepository;
    private final SeasonRepository seasonRepository;

    /**
     * get raking list with specific season id and page 
     * @param seasonId
     * @param page
     * @param pageSize
     * @return UserRankings
     */
    public List<UserRanking> getRankingWithPage(long seasonId, int page, int pageSize) {
        // invalid season id
        Optional<Season> season = seasonRepository.findById(seasonId);
        if(!season.isPresent()) return null;
    
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("score").descending());
        List<SeasonRanking> rankings = rankingRepository.findBySeasonId(seasonId, pageable);

        // make list with dto class
        List<UserRanking> userRankings = new ArrayList<>();
        for(SeasonRanking seasonRanking : rankings)
            userRankings.add(new UserRanking(seasonRanking));

        return userRankings;
    }

    /**
     * get total page of ranking with specific season
     */
    public long getRankingTotalPages(long seasonId, int pageSize) {
        // invalid season id
        Optional<Season> season = seasonRepository.findById(seasonId);
        if(!season.isPresent()) return 0;

        long totalCount = rankingRepository.countBySeasonId(seasonId);
        long totalPages = (long) Math.ceil((double) totalCount / pageSize);

        return totalPages;
    }
}
