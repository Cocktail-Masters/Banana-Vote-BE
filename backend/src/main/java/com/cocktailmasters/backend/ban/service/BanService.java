package com.cocktailmasters.backend.ban.service;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.ban.controller.dto.BanLogResponse;
import com.cocktailmasters.backend.ban.domain.entity.BanLog;
import com.cocktailmasters.backend.ban.domain.repository.BanLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BanService {

    private final BanLogRepository banLogRepository;
    private final UserRepository userRepository;

    /**
     * get ban log list
     * 
     * @return ban log response list
     */
    @Transactional
    public List<BanLogResponse> getBanLogs() {
        List<BanLog> banLogs = banLogRepository.findAll();

        List<BanLogResponse> banLogsDtos = new ArrayList<>();
        for (BanLog banLog : banLogs)
            banLogsDtos.add(BanLogResponse.createLogResponse(banLog));

        return banLogsDtos;
    }

    /**
     * ban user
     * 
     * @return true or false(if user not found or user is admin)
     */
    @Transactional
    public boolean banUser(long userId, String banReason) {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent())
            return false;
        else {
            // change role
            user.get().banUser();

            // add log
            BanLog banLog = BanLog.builder()
                    .banReason(banReason)
                    .user(user.get())
                    .build();

            banLogRepository.save(banLog);
            return true;
        }
    }

    /**
     * unban user
     * 
     * @param userId
     * @return true or false(user not found)
     */
    @Transactional
    public boolean unbanUser(long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent())
            return false;
        else {
            // change role
            user.get().updateRoleToUser();
            userRepository.save(user.get());
            return true;
        }
    }
}
