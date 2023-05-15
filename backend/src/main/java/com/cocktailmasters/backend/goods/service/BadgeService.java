package com.cocktailmasters.backend.goods.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.goods.controller.dto.BadgeRequest;
import com.cocktailmasters.backend.goods.controller.dto.BadgeResponse;
import com.cocktailmasters.backend.goods.controller.dto.item.BadgeItemDto;
import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.cocktailmasters.backend.goods.domain.repository.BadgeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;

    /**
     * make badge with request (only adimin)
     * 
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

        if (badgeRepository.save(badge) != null)
            return true;
        else
            return false;
    }

    /**
     * Modifiy Badge(for admin)
     * 
     * @param badgeId
     * @param badgeRequest user Request
     * @return -1 (badge not found) or 0 (db not ok) or 1 (success)
     */
    @Transactional
    public int modifiyBadge(Long badgeId, BadgeRequest badgeRequest) {
        Optional<Badge> badge = badgeRepository.findById(badgeId);
        if (!badge.isPresent())
            return -1;

        badge.get().modifiyBadgeWithDto(badgeRequest);

        if (badgeRepository.save(badge.get()) != null)
            return 1;
        else
            return 0;
    }

    /**
     * delete Badge(for admin)
     * 
     * @param badgeId
     * @return true or false (badge not found)
     */
    @Transactional
    public boolean deleteBadge(Long badgeId) {
        Optional<Badge> badge = badgeRepository.findById(badgeId);
        if (!badge.isPresent())
            return false;

        badgeRepository.deleteById(badgeId);
        return true;
    }

    /**
     * get Badge list with options and paging
     * 
     * @param isSelling
     * @return List of badge Response
     */
    public BadgeResponse getBadgeList(boolean isSelling, int sortBy, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        List<Badge> badgeList;
        if (isSelling) {
            // get is seliing badges
            switch (sortBy) {
                case 1:
                default:
                    badgeList = badgeRepository
                            .findByBadgeEndDateAfterAndIsSellingOrderByBadgeEndDateAsc(LocalDate.now(), true, pageable)
                            .getContent();
                    break;
                case 2:
                    badgeList = badgeRepository
                            .findByBadgeEndDateAfterAndIsSellingOrderByBadgeSoldNumberAsc(LocalDate.now(), true,
                                    pageable)
                            .getContent();
                    break;
                case 3:
                    badgeList = badgeRepository
                            .findByBadgeEndDateAfterAndIsSellingOrderByBadgePriceAsc(LocalDate.now(), true,
                                    pageable)
                            .getContent();
                    break;
                case 4:
                    badgeList = badgeRepository
                            .findByBadgeEndDateAfterAndIsSellingOrderByBadgePriceDesc(LocalDate.now(), true,
                                    pageable)
                            .getContent();
                    break;
            }
        } else {
            // all
            switch (sortBy) {
                case 1:
                default:
                    badgeList = badgeRepository.findAllByOrderByBadgeEndDateAsc(pageable).getContent();
                    break;
                case 2:
                    badgeList = badgeRepository.findAllByOrderByBadgeSoldNumberAsc(pageable).getContent();
                    break;
                case 3:
                    badgeList = badgeRepository.findAllByOrderByBadgePriceAsc(pageable).getContent();
                    break;
                case 4:
                    badgeList = badgeRepository.findAllByOrderByBadgePriceDesc(pageable).getContent();
                    break;
            }
        }

        List<BadgeItemDto> badgeDtos = new ArrayList<>();
        for (Badge badge : badgeList)
            badgeDtos.add(BadgeItemDto.createBadgeReponse(badge));

        // get total page
        int totalPage = getGoodsListTotalPages(isSelling, pageSize);

        return new BadgeResponse(totalPage, badgeDtos);
    }

    /**
     * count total page size of badges
     * 
     * @param isSelling if false it take all
     * @param pageSize
     * @return total page size
     */
    public int getGoodsListTotalPages(boolean isSelling, int pageSize) {
        long totalCount;
        if (!isSelling)
            totalCount = badgeRepository.count();
        else
            totalCount = badgeRepository.countByBadgeEndDateAfterAndIsSelling(LocalDate.now(), true);

        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
