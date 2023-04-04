package com.cocktailmasters.backend.point.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.point.controller.dto.PointLogResponse;
import com.cocktailmasters.backend.point.domain.entity.PointLog;
import com.cocktailmasters.backend.point.domain.repository.PointLogRepository;
import com.cocktailmasters.backend.season.service.RankingService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PointLogService {
    
    private final PointLogRepository pointLogRepository;
    private final UserRepository userRepository;

    private final RankingService rankingService;

    /**
     * get Point Logs by user Id
     * @param userId
     * @return List of point logs
     */
    public List<PointLogResponse> getPointLogs(long userId) {
        List<PointLog> pointLogs = pointLogRepository.findAllByUserId(userId);

        List<PointLogResponse> pointLogResponses = new ArrayList<>();
        for(PointLog pointLog : pointLogs)
            pointLogResponses.add(new PointLogResponse(pointLog));

        return pointLogResponses;
    }

    /**
     * add point to user with log
     * if season is on, it will modify season score too
     * @param amount increase or decrease value of mount
     * @param description
     * @param userId
     * @return true for success or false
     */
    @Transactional
    public boolean modifyPoint(long amount, String description, long userId) {
        Optional<User> userInfo = userRepository.findById(userId);
        if(!userInfo.isPresent()) return false;

        int res = userRepository.updateUserPointById(userId, userInfo.get().getPoints() + amount);
        if(res == 0) return false;

        addPointLog(amount, description, userInfo.get());
        if(amount > 0) rankingService.addCurrentSeasonScore(amount, userInfo.get());

        return true;
    }


    /**
     * add point log with User object
     * @param amount
     * @param description
     * @param user
     * @return added Point log object
     */
    @Transactional
    public PointLog addPointLog(long amount, String description, User user) {
        if(user == null) return null;

        PointLog addedPointLog = PointLog.builder()
            .user(user)
            .amount(amount)
            .description(description)
            .build();

        return pointLogRepository.save(addedPointLog);
    }

    /**
     * add point log with user Id
     * @param amount
     * @param decription
     * @param userId
     * @return added Point log object
     */
    @Transactional
    public PointLog addPointLog(long amount, String decription, long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()) return null;
        else return addPointLog(amount, decription, user.get());
    }
}
