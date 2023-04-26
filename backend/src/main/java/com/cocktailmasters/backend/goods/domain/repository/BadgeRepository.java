package com.cocktailmasters.backend.goods.domain.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByBadgeEndDateBeforeAndIsSelling(LocalDate date, boolean isSelling);
    List<Badge> findAllByIsSelling(boolean isSelling);
}
