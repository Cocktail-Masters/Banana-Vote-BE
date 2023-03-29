package com.cocktailmasters.backend.season.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
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
    public RankingResponse getRankingListWithPage(long seasonId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<SeasonRanking> rankingsWithPage = rankingRepository.findBySeasonIdOrderByScoreDesc(seasonId, pageable);
        List<SeasonRanking> seasonRankings = rankingsWithPage.getContent(); // with entity

        // make list with dto class
        List<UserRanking> userRankings = new ArrayList<>();
        for(int i = 0; i < seasonRankings.size(); i++)
            userRankings.add(new UserRanking(seasonRankings.get(i), page * pageSize + i));

        return new RankingResponse(rankingsWithPage.getTotalPages(), userRankings);
    }

    /**
     * get total page of ranking with specific season
     * @return total pages
     */
    public int getRankingTotalPages(long seasonId, int pageSize) {
        long totalCount = rankingRepository.countBySeasonId(seasonId);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        return totalPages;
    }

    /**
     * get raking page number with specific season id and nickname 
     * @param seasonId
     * @param pageSize
     * @param nickname 
     * @return UserRankings page number containing nickname parameter or -1 if user Ranking is invalid
     */
    public int getRankingPageNumberByNickname(long seasonId, int pageSize, String nickname) {
        long userRanking = getUserRanking(seasonId, nickname);
        if(userRanking == -1) return -1; // nickname not found

        return (int) Math.floor((double) userRanking / pageSize);
    }

    /**
     * get user ranking with seasonid and nickname
     * @param seasonId
     * @param nickname
     * @return user ranking or -1 if nickname or seasonId is invalid
     */
    public long getUserRanking(long seasonId, String nickname) {
        Optional<Long> userScore = rankingRepository.findScoreBySeasonIdAndNickname(seasonId, nickname);
        if(!userScore.isPresent()) return -1;

        long userRanking = rankingRepository.getUserRankingByScore(seasonId, userScore.get());
        return userRanking;
    }

    /**
     * check season is valid
     * @param seasonId
     * @return true or false
     */
    public boolean isValidSeason(long seasonId) {
        // invalid season id
        Optional<Season> season = seasonRepository.findById(seasonId);
        if(season.isPresent())
            return true;
        else
            return false;
    }
}
