package com.cocktailmasters.backend.authentication.service;

import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.authentication.domain.AuthTokensGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuthLoginService {

    private final UserRepository userRepository;
    private final AuthTokensGenerator authTokensGenerator;
}
