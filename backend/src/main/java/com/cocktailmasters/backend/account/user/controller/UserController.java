package com.cocktailmasters.backend.account.user.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.user.controller.dto.SignUpRequest;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Tag(name = "user", description = "회원 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;

    @Operation(summary = "일반 회원 가입", description = "일반 회원 가입")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest signUpRequest) throws Exception {
        if (userService.signUp(signUpRequest)) {
            log.info("successful membership");
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/jwt-test")
    public String jwtTest(@RequestHeader(AUTHORIZATION) String token) {
        User user = jwtService.findUserByToken(token);
        return "jwtTest 요청 성공";
    }
}
