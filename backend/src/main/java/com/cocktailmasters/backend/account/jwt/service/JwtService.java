package com.cocktailmasters.backend.account.jwt.service;

import com.cocktailmasters.backend.account.jwt.infra.JwtProvider;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;



}
