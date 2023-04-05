package com.cocktailmasters.backend.point.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.season.service.RankingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PointService {

    private final UserRepository userRepository;

    private final RankingService rankingService;
    private final PointLogService pointLogService;

    /**
     * add point to user with log
     * if season is on, it will modify season score too
     * @param amount increase or decrease value of mount
     * @param description
     * @param user
     * @return true for success or false
     */
    @Transactional
    public boolean addPoint(long amount, String description, long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return false;

        int res = userRepository.updateUserPointById(user.get().getId(), user.get().getPoints() + amount);
        if(res == 0) return false;

        pointLogService.addPointLog(amount, description, user.get()); // add log
        if(amount > 0) rankingService.addCurrentSeasonScore(amount, user.get());  // score update

        return true;
    }

    /**
     * add point to user with log (with nickname)
     * if season is on, it will modify season score too
     * @param amount increase or decrease value of mount
     * @param description
     * @param user
     * @return true for success or false
     */
    @Transactional
    public boolean addPoint(long amount, String description, String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        if(!user.isPresent()) return false;

        int res = userRepository.updateUserPointById(user.get().getId(), user.get().getPoints() + amount);
        if(res == 0) return false;

        pointLogService.addPointLog(amount, description, user.get()); // add log
        if(amount > 0) rankingService.addCurrentSeasonScore(amount, user.get());  // score update

        return true;
    }

    /**
     * get Point amount by nickname
     * @param nickname
     * @return -1 or points
     */
    public long getPoint(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);

        if(!user.isPresent()) return -1;
        else return user.get().getPoints();
    }
}
