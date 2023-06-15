package com.cocktailmasters.backend.goods.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.UserBadge;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findAllByUserId(long userId);

    Optional<UserBadge> findByBadgeIdAndUserId(long BadgeId, long UserId);

    Optional<UserBadge> findByUserIdAndIsEquipped(long userId, boolean isEquipped);
}
