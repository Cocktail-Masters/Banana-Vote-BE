package com.cocktailmasters.backend.goods.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.goods.controller.dto.UserBadgeResponse;
import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.cocktailmasters.backend.goods.domain.entity.UserBadge;
import com.cocktailmasters.backend.goods.domain.repository.BadgeRepository;
import com.cocktailmasters.backend.goods.domain.repository.UserBadgeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserBadgeService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    /**
     * get users all badges list by user id
     * 
     * @param userId
     * @return badge of user
     */
    @Transactional
    public List<UserBadgeResponse> getUserBadgesList(long userId) {
        List<UserBadge> userBadges = userBadgeRepository.findAllByUserId(userId);

        List<UserBadgeResponse> userBadgesDtos = new ArrayList<>();
        for (UserBadge badge : userBadges) {
            userBadgesDtos.add(UserBadgeResponse.createUserBadgeResponse(badge));
        }

        return userBadgesDtos;
    }

    /**
     * add badge to user
     * 
     * @param badgeId
     * @param userId
     * @return 1 or 0, -1(user or badge not existed, or badge already had)
     */
    @Transactional
    public int addBadgeToUser(long badgeId, long userId) {
        Optional<Badge> addedBadge = badgeRepository.findById(badgeId);
        Optional<User> targetUser = userRepository.findById(userId);
        Optional<UserBadge> userBadge = userBadgeRepository.findByBadgeIdAndUserId(badgeId, userId);

        if (!addedBadge.isPresent() || !targetUser.isPresent())
            return 0;
        if (userBadge.isPresent())
            return -1;

        UserBadge addedUserBadge = UserBadge.builder()
                .isEquipped(false)
                .badge(addedBadge.get())
                .user(targetUser.get())
                .build();

        if (userBadgeRepository.save(addedUserBadge) != null)
            return 1;
        else
            return -1;
    }

    /**
     * delete badge from user
     * 
     * @param badgeId
     * @param userId
     * @return true or false(user or badge not existed, or badge already had)
     */
    @Transactional
    public boolean deleteBadgeFromUser(long badgeId, long userId) {
        Optional<Badge> addedBadge = badgeRepository.findById(badgeId);
        Optional<User> targetUser = userRepository.findById(userId);
        Optional<UserBadge> userBadge = userBadgeRepository.findByBadgeIdAndUserId(badgeId, userId);

        if (!addedBadge.isPresent() || !targetUser.isPresent() || !userBadge.isPresent())
            return false;

        userBadgeRepository.delete(userBadge.get());
        return true;
    }

    /**
     * equip badge
     * 
     * @param badgeId want to equip
     * @param userId
     * @return true or false(badge not found)
     */
    @Transactional
    public boolean changeEquippedBadge(long userBadgeId, long userId) {
        Optional<UserBadge> targetBadge = userBadgeRepository.findById(userBadgeId);
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent() || !targetBadge.isPresent())
            return false;

        // upequip badge
        Optional<UserBadge> equippedBadge = userBadgeRepository.findByUserIdAndIsEquipped(userId, true);
        if (equippedBadge.isPresent()) {
            equippedBadge.get().unequipBadge();
            userBadgeRepository.save(equippedBadge.get());
        }

        // equip badge
        targetBadge.get().equipBadge();
        userBadgeRepository.save(targetBadge.get());

        // update badge image
        user.get().updateBadgeImage(targetBadge.get().getBadge().getBadgeImageUrl());
        userRepository.save(user.get());
        return true;
    }
}
