package com.cocktailmasters.backend.goods.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.goods.controller.dto.BadgeRequest;
import com.cocktailmasters.backend.goods.controller.dto.BadgeResponse;
import com.cocktailmasters.backend.goods.controller.dto.item.BadgeItemDto;
import com.cocktailmasters.backend.goods.domain.entity.Badge;
import com.cocktailmasters.backend.goods.domain.repository.BadgeRepository;
import com.cocktailmasters.backend.point.service.PointService;
import com.cocktailmasters.backend.util.exception.CustomException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;

    private final UserBadgeService userBadgeService;
    private final PointService pointService;

    /**
     * make badge with request (only adimin)
     * 
     * @param badgeRequest
     * @return true or false
     */
    @Transactional
    public boolean makeBadge(BadgeRequest badgeRequest) {
        Badge badge = Badge.builder()
                .badgeName(badgeRequest.getName())
                .badgeImageUrl(badgeRequest.getImageUrl())
                .badgeDescription(badgeRequest.getDescription())
                .badgePrice(badgeRequest.getPrice())
                .isSelling(badgeRequest.getIsSelling())
                .badgeEndDate(badgeRequest.getBadgeEndDate())
                .build();

        if (badgeRepository.save(badge) != null)
            return true;
        else
            return false;
    }

    /**
     * buy badge and add to user
     * 
     * @param badgeId
     * @param userId  who buy badge
     * @throws CustomException CONFLICT with point (lack of point or badge already
     *                         had)
     * @return 1 (success), 0 (badge not found), -1 (not selling, saling end)
     */
    @Transactional
    public int buyBadge(long badgeId, long userId) {
        Optional<Badge> badge = badgeRepository.findById(badgeId);
        if (!badge.isPresent())
            return 0;

        // check selling date
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(badge.get().getBadgeEndDate()) || !badge.get().isSelling())
            return -1;

        // apply point
        String logMessage = badge.get().getBadgeName() + " 구매";
        if (!pointService.addPoint(badge.get().getBadgePrice() * -1, logMessage, userId))
            throw new CustomException(HttpStatus.CONFLICT, "lack of point");

        // add to user
        int result = userBadgeService.addBadgeToUser(badgeId, userId);
        if (result == 0)
            throw new CustomException(HttpStatus.NOT_FOUND, "user or badge not found");
        else if (result == -1)
            throw new CustomException(HttpStatus.CONFLICT, "user already had badge");

        badge.get().soldBadge();
        badgeRepository.save(badge.get());
        return 1;
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
                            .findByBadgeEndDateAfterAndIsSellingOrderByBadgeEndDateDesc(LocalDate.now(), true, pageable)
                            .getContent();
                    break;
                case 2:
                    badgeList = badgeRepository
                            .findByBadgeEndDateAfterAndIsSellingOrderByBadgeSoldNumberDesc(LocalDate.now(), true,
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
                    badgeList = badgeRepository.findAllByOrderByBadgeEndDateDesc(pageable).getContent();
                    break;
                case 2:
                    badgeList = badgeRepository.findAllByOrderByBadgeSoldNumberDesc(pageable).getContent();
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
