package com.cocktailmasters.backend.season.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.season.controller.dto.SeasonDto;
import com.cocktailmasters.backend.season.domain.entity.Season;
import com.cocktailmasters.backend.season.domain.repository.SeasonRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;

    /*
     * get Current Season by server time
     */
    public Season getCurrentSeason() {
        LocalDate current = LocalDate.now();

        for(Season season : seasonRepository.findAll(Sort.by(Sort.Direction.ASC, "seasonStartDate"))) {
            LocalDate startDate = season.getSeasonStartDate();
            LocalDate endDate = season.getSeasonEndDate();

            // between start and end
            if(!startDate.isAfter(current) && !endDate.isBefore(current)) return season;
        }

        return null;
    }

    /**
     * Latest ended season
     * @return the latest season info if any season is gone return null
     */
    public Season getLatestSeason() {
        LocalDate current = LocalDate.now();

        for(Season season : seasonRepository.findAll(Sort.by(Sort.Direction.DESC, "seasonEndDate"))) {
            LocalDate startDate = season.getSeasonStartDate();
            LocalDate endDate = season.getSeasonEndDate();

            // between start and end
            if(!startDate.isAfter(current) && !endDate.isBefore(current)) return season;
            // latest season
            if(endDate.isBefore(current)) return season;
        }

        return Season.builder().id(-1L).build();
    }

    /**
     * get information of all season
     */
    public List<Season> getSeasons() {
        return seasonRepository.findAll(Sort.by(Sort.Direction.ASC, "seasonStartDate"));
    }

    @Transactional
    public boolean addSeason(SeasonDto season) {
        return addSeason(season, false);
    }

    /**
     * add Season data to DB
     * @param season
     * @param isCheckingDateRange for overlapped date checking
     * @return true or false by success transaction
     */
    @Transactional
    public boolean addSeason(SeasonDto season, boolean isCheckingDateRange) {
        Season addedSeason = Season.builder()
            .seasonStartDate(season.getStartDate())
            .seasonEndDate(season.getEndDate())
            .seasonDescription(season.getDescription())
            .build();

        // checking overlap date with parameter season
        if(isCheckingDateRange && isOverlapWithAllSeason(addedSeason))
            return false;

        seasonRepository.save(addedSeason);
        return true;
    }

    /**
     * check all season with date is overlapped
     * except me
     */
    private boolean isOverlapWithAllSeason(Season season) {
        LocalDate startDate = season.getSeasonStartDate();
        LocalDate endDate = season.getSeasonEndDate();

        List<Season> allSeasons = getSeasons();
        for(Season currentSeason : allSeasons) {
            // not compare by me
            if(currentSeason.getId() == season.getId()) continue;

            LocalDate curStartDate = currentSeason.getSeasonStartDate();
            LocalDate curEndDate = currentSeason.getSeasonEndDate();

            if((!startDate.isAfter(curStartDate) && !endDate.isBefore(curStartDate))
                || (!startDate.isAfter(curEndDate) && !endDate.isBefore(curEndDate)))
                return true;
        }
        return false;
    }

    /**
     * modify Season
     * @param season
     * @param isCheckingDateRange for overlapped date checking
     * @return true of false by success transaction
     */
    @Transactional
    public boolean modifySeason(SeasonDto season, boolean isCheckingDateRange) {
        Optional<Season> optionalSeason = seasonRepository.findById(season.getId());
        if(!optionalSeason.isPresent())
            return false;

        Season modifiedSeason = optionalSeason.get();
        modifiedSeason = Season.builder()
            .id(modifiedSeason.getId())
            .seasonStartDate(season.getStartDate() == null? modifiedSeason.getSeasonStartDate() : season.getStartDate())
            .seasonEndDate(season.getEndDate() == null? modifiedSeason.getSeasonEndDate() : season.getEndDate())
            .seasonDescription(season.getDescription())
            .build();

        // checking overlap date with parameter season
        if(isCheckingDateRange && isOverlapWithAllSeason(modifiedSeason))
            return false;
        if(modifiedSeason.getSeasonEndDate().isBefore(modifiedSeason.getSeasonStartDate()))
            return false;
        
        seasonRepository.save(modifiedSeason);
        return true;
    }
}
