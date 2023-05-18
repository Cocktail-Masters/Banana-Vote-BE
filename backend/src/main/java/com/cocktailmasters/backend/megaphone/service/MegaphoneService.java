package com.cocktailmasters.backend.megaphone.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.megaphone.domain.dto.MegaphoneResponse;
import com.cocktailmasters.backend.megaphone.domain.entity.Megaphone;
import com.cocktailmasters.backend.megaphone.domain.repository.MegaphoneRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MegaphoneService {

    private final MegaphoneRepository megaphoneRepository;

    /**
     * get list of megaphone
     * active is determined by now server time
     * 
     * @param isActive
     * @return list of megaphone dto
     */
    public List<MegaphoneResponse> getMegaphones(boolean isActive) {
        List<Megaphone> megaphones;
        if (isActive)
            megaphones = megaphoneRepository.findByMegaphoneEndDateTimeAfter(LocalDateTime.now());
        else
            megaphones = megaphoneRepository.findAll();

        List<MegaphoneResponse> megaphoneDtos = new ArrayList<>();
        for (Megaphone megaphone : megaphones)
            megaphoneDtos.add(MegaphoneResponse.createMegaphoneResponse(megaphone));
        return megaphoneDtos;
    }

    /**
     * delete megaphone (for admin)
     * 
     * @param megaphoneId
     * @return true or false(megaphone not found)
     */
    public boolean deleteMegaphones(long megaphoneId) {
        Optional<Megaphone> megaphone = megaphoneRepository.findById(megaphoneId);
        if (!megaphone.isPresent())
            return false;

        megaphoneRepository.delete(megaphone.get());
        return true;
    }
}
