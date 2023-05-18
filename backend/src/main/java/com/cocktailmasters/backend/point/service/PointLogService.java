package com.cocktailmasters.backend.point.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.point.controller.dto.PointLogResponse;
import com.cocktailmasters.backend.point.domain.entity.PointLog;
import com.cocktailmasters.backend.point.domain.repository.PointLogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PointLogService {

    private final PointLogRepository pointLogRepository;
    private final UserRepository userRepository;

    /**
     * get Point Logs by user Id
     * 
     * @param userId
     * @return List of point logs
     */
    public List<PointLogResponse> getPointLogs(long userId) {
        List<PointLog> pointLogs = pointLogRepository.findAllByUserId(userId);

        List<PointLogResponse> pointLogResponses = new ArrayList<>();
        for (PointLog pointLog : pointLogs)
            pointLogResponses.add(new PointLogResponse(pointLog));

        return pointLogResponses;
    }

    /**
     * add point log with User object
     * 
     * @param amount
     * @param description
     * @param user
     * @return added Point log object
     */
    @Transactional
    public PointLog addPointLog(long amount, String description, User user) {
        if (user == null)
            return null;

        PointLog addedPointLog = PointLog.builder()
                .user(user)
                .amount(amount)
                .description(description)
                .build();

        return pointLogRepository.save(addedPointLog);
    }

    /**
     * add point log with user Id
     * 
     * @param amount
     * @param description
     * @param userId
     * @return added Point log object
     */
    @Transactional
    public PointLog addPointLog(long amount, String description, long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent())
            return null;
        else
            return addPointLog(amount, description, user.get());
    }
}
