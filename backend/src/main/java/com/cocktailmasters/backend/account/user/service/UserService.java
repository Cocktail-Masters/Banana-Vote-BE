package com.cocktailmasters.backend.account.user.service;

import com.cocktailmasters.backend.account.user.controller.dto.SignUpRequest;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signUp(SignUpRequest signUpRequest) throws Exception {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new Exception("This email already exists");
        }
        if (userRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            throw new Exception("This nickname already exists");
        }
        User user = signUpRequest.toUserEntity();
        user.encodePassword(passwordEncoder);
        userRepository.save(user);
        return true;
    }
}
