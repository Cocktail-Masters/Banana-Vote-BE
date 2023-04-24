package com.cocktailmasters.backend.goods.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.goods.controller.dto.BadgeRequest;
import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.cocktailmasters.backend.goods.domain.repository.BadgeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BadgesService {

    private final BadgeRepository badgeRepository;
    
    /**
     * make badge with request (only adimin)
     * @param badgeRequest
     * @return true or false
     */
    public boolean makeBadge(BadgeRequest badgeRequest) {
        Badge badge = Badge.builder()
            .badgeName(badgeRequest.getName())
            .badgeImageUrl(badgeRequest.getImageUrl())
            .badgeDescription(badgeRequest.getDescription())
            .badgePrice(badgeRequest.getPrice())
            .isSelling(badgeRequest.isSelling())
            .badgeEndDate(badgeRequest.getBadgeEndDate())
            .build();

        if(badgeRepository.save(badge) != null) return true;
        else return false;
    }

    /**
     * Modifiy Badge(for admin)
     * @param badgeId
     * @param badgeRequest user Request
     * @return -1 (badge not found) or 0 (db not ok) or 1 (success)
     */
    @Transactional
    public int modifiyBadge(Long badgeId, BadgeRequest badgeRequest) {
        Optional<Badge> badge = badgeRepository.findById(badgeId);
        if(!badge.isPresent()) return -1;

        badge.get().modifiyBadgeWithDto(badgeRequest);

        if(badgeRepository.save(badge.get()) != null) return 1;
        else return 0;
    }

    /**
     * delete Badge(for admin)
     * @param badgeId
     * @return true or false (badge not found)
     */
    @Transactional
    public boolean deleteBadge(Long badgeId) {
        Optional<Badge> badge = badgeRepository.findById(badgeId);
        if(!badge.isPresent()) return false;

        badgeRepository.deleteById(badgeId);
        return true;
    }
}
