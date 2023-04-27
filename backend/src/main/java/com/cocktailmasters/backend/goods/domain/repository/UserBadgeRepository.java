package com.cocktailmasters.backend.goods.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.UserBadge;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    List<UserBadge> findAllByUserId(long userId);
}
