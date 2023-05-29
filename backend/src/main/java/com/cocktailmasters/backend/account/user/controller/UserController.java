package com.cocktailmasters.backend.account.user.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.user.controller.dto.SignUpRequest;
import com.cocktailmasters.backend.account.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "일반 로그인", description = "일반 로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn() {
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "로그아웃", description = "로그아웃")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut() {
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @DeleteMapping("")
    public ResponseEntity<String> withdrawal() {
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보 조회, " +
            "다른 사람 조회 시 닉네임, 랭킹, 퍼센테이지 / 나의 회원 정보 조회 시 추가로 나이, 성별")
    @GetMapping("/{user-id}")
    public ResponseEntity<String> findUserInfo(@PathVariable("user-id") Long userId) {
        return ResponseEntity.badRequest().build();
    }
}
