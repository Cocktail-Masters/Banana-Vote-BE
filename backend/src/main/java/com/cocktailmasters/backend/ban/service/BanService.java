package com.cocktailmasters.backend.ban.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.ban.controller.dto.BanLogResponse;
import com.cocktailmasters.backend.ban.domain.entity.BanLog;
import com.cocktailmasters.backend.ban.domain.repository.BanLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BanService {

    private final BanLogRepository banLogRepository;

    /**
     * get ban log list
     * 
     * @return ban log response list
     */
    public List<BanLogResponse> getBanLogs() {
        List<BanLog> banLogs = banLogRepository.findAll();

        List<BanLogResponse> banLogsDtos = new ArrayList<>();
        for (BanLog banLog : banLogs)
            banLogsDtos.add(BanLogResponse.createLogReponse(banLog));

        return banLogsDtos;
    }
}
