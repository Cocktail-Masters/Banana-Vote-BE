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
        if(!user.isPresent() || user.get().getPoints() + amount < 0) return false;

        // update point
        if(userRepository.updateUserPointById(user.get().getId(), user.get().getPoints() + amount) == 0) return false;
        
        pointLogService.addPointLog(amount, description, user.get()); // add log
        if(amount > 0) rankingService.addCurrentSeasonScore(amount, user.get());  // score update

        return true;
    }

    /**
     * add point to user with log only used by admin
     * @param value increase or decrease value of mount
     * @param description
     * @param user
     * @return true for success or false
     */
    @Transactional
    public boolean modifyPoint(long value, long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return false;

        long prev_point = user.get().getPoints();
        int res = userRepository.updateUserPointById(user.get().getId(), value);
        if(res == 0) return false;

        pointLogService.addPointLog(prev_point - value, "modified by admin", user.get()); // add log

        return true;
    }

    /**
     * get Point amount by nickname
     * @param nickname
     * @return -1 or points
     */
    public long getPoint(long userId) {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) return -1;
        else return user.get().getPoints();
    }
}
