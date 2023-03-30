package com.cocktailmasters.backend.point.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.point.controller.dto.PointLogResponse;
import com.cocktailmasters.backend.point.domain.entity.PointLog;
import com.cocktailmasters.backend.point.domain.repository.PointLogRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PointLogService {
    
    private final PointLogRepository pointLogRepository;

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
}
