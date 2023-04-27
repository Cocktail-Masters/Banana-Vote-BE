package com.cocktailmasters.backend.account.service;

import com.cocktailmasters.backend.account.controller.dto.UserSignUpRequest;
import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(UserSignUpRequest userSignUpRequest) throws Exception {
        if (userRepository.findByEmail(userSignUpRequest.getEmail()).isPresent()) {
            throw new Exception();
        }
        if (userRepository.findByNickname(userSignUpRequest.getEmail()).isPresent()) {
            throw new Exception();
        }
        User user = userSignUpRequest.toUserEntity();
        user.encodePassword(passwordEncoder);
        userRepository.save(user);
    }
}
