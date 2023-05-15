package com.cocktailmasters.backend.goods.domain.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.Badge;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

        Page<Badge> findByBadgeEndDateAfterAndIsSellingOrderByBadgeEndDateAsc(LocalDate date, boolean isSelling,
                        Pageable pageable);

        Page<Badge> findByBadgeEndDateAfterAndIsSellingOrderByBadgeSoldNumberAsc(LocalDate date, boolean isSelling,
                        Pageable pageable);

        Page<Badge> findByBadgeEndDateAfterAndIsSellingOrderByBadgePriceAsc(LocalDate date, boolean isSelling,
                        Pageable pageable);

        Page<Badge> findByBadgeEndDateAfterAndIsSellingOrderByBadgePriceDesc(LocalDate date, boolean isSelling,
                        Pageable pageable);

        Page<Badge> findAllByOrderByBadgeEndDateAsc(Pageable pageable);

        Page<Badge> findAllByOrderByBadgeSoldNumberAsc(Pageable pageable);

        Page<Badge> findAllByOrderByBadgePriceAsc(Pageable pageable);

        Page<Badge> findAllByOrderByBadgePriceDesc(Pageable pageable);

        long countByBadgeEndDateAfterAndIsSelling(LocalDate date, boolean isSelling);
}
