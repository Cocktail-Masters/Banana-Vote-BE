package com.cocktailmasters.backend.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.goods.controller.dto.UserBadgeResponse;
import com.cocktailmasters.backend.goods.domain.entity.UserBadge;
import com.cocktailmasters.backend.goods.domain.repository.UserBadgeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserBadgeService {
    
    private final UserBadgeRepository userBadgeRepository;

    /**
     * get users all badges list by user id
     * @param userId
     * @return badge of user
     */
    public List<UserBadgeResponse> getUserBadgesList(long userId) {
        List<UserBadge> userBadges = userBadgeRepository.findAllByUserId(userId);

        List<UserBadgeResponse> userBadgesDtos = new ArrayList<>();
        for(UserBadge badge : userBadges) {
            userBadgesDtos.add(UserBadgeResponse.createUserBadgeResponse(badge));
        }

        return userBadgesDtos;
    }
}
