package com.cocktailmasters.backend.goods.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long> { }
