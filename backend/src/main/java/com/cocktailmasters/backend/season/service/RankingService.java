package com.cocktailmasters.backend.season.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
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
    private final UserRepository userRepository;

    private final SeasonService seasonService;

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

        return new RankingResponse(rankingsWithPage.getTotalPages(), page, userRankings);
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
        Optional<User> user = userRepository.findByNickname(nickname);
        if(!user.isPresent()) return -1; // not found user

        long userRanking = -1;
        try {
            long userScore = rankingRepository.findScoreBySeasonIdAndUser(seasonId, user.get().getId());
            userRanking = rankingRepository.countUserRankingByScore(seasonId, userScore);
        } catch(EmptyResultDataAccessException ex) {
            userRanking = -1;
        }
        
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

    /**
     * add score to current season
     * @param amount must be positive number
     * @return true of false by success
     */
    @Transactional
    public boolean addCurrentSeasonScore(long amount, User user) {
        if(amount < 0) return false;

        // check current season
        Season currentSeason = seasonService.getCurrentSeason();
        if(currentSeason == null) return false;

        Optional<SeasonRanking> seasonRanking = rankingRepository.findBySeasonIdAndUserId(currentSeason.getId(), user.getId());

        SeasonRanking modifiedSeasonRanking = null;
        if(seasonRanking.isPresent()) {
            modifiedSeasonRanking = seasonRanking.get();
            modifiedSeasonRanking.setScore(modifiedSeasonRanking.getScore() + amount);
        } else {
            // newly add user score column
            modifiedSeasonRanking = SeasonRanking.builder()
                .score(amount)
                .season(currentSeason)
                .user(user)
                .build();
        }

        rankingRepository.save(modifiedSeasonRanking);
        return true;
    }
}
