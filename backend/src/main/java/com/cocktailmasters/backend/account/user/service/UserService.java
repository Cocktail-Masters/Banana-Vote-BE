package com.cocktailmasters.backend.account.user.service;

import com.cocktailmasters.backend.account.user.controller.dto.FindUserInfoResponse;
import com.cocktailmasters.backend.account.user.controller.dto.SignInRequest;
import com.cocktailmasters.backend.account.user.controller.dto.SignUpRequest;
import com.cocktailmasters.backend.account.user.controller.dto.UpdateNicknameRequest;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.season.service.RankingService;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final RankingService rankingService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signUp(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return false;
        }
        if (userRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            return false;
        }
        User user = signUpRequest.toUserEntity();
        user.encodePassword(passwordEncoder);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmailAndIsActiveTrue(signInRequest.getEmail())
                .orElse(null);
        if (user == null) {
            return false;
        }
        if (passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            return true;
        }
        return false;
    }

    public boolean signOut(User user) {
        user.updateRefreshToken("");
        return true;
    }

    public boolean withdrawal(User user) {
        user.deleteUser();
        userRepository.save(user);
        return true;
    }

    public FindUserInfoResponse findUserInfo(User user, long findUserId) {
        if (user != null && user.getId() == findUserId) {
            return FindUserInfoResponse.builder()
                    .nickname(user.getNickname())
                    .equippedBadgeImageUrl(user.getEquippedBadgeImageUrl())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .ranking(rankingService.getCurrentSeasonUserRanking(user.getId()))
                    .points(user.getPoints())
                    .build();
        }
        User findUser = userRepository.findById(findUserId)
                .orElse(null);
        if (findUser == null) {
            throw new NotFoundUserException();
        }
        return FindUserInfoResponse.builder()
                .nickname(findUser.getNickname())
                .equippedBadgeImageUrl(findUser.getEquippedBadgeImageUrl())
                .ranking(rankingService.getCurrentSeasonUserRanking(findUser.getId()))
                .build();
    }

    @Transactional
    public boolean updateNickname(User user, UpdateNicknameRequest updateNicknameRequest) {
        String newNickname = updateNicknameRequest.getNickname();
        if (!userRepository.findByNickname(newNickname).isEmpty()) {
            return false;
        }
        user.updateNickname(updateNicknameRequest.getNickname());
        user.updateRoleGuestToUser();
        userRepository.save(user);
        return true;
    }
}
