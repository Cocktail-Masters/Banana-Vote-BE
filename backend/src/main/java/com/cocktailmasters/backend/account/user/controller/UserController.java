package com.cocktailmasters.backend.account.user.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.user.controller.dto.FindUserInfoResponse;
import com.cocktailmasters.backend.account.user.controller.dto.SignInRequest;
import com.cocktailmasters.backend.account.user.controller.dto.SignUpRequest;
import com.cocktailmasters.backend.account.user.controller.dto.UpdateNicknameRequest;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.service.UserService;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

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
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        if (userService.signUp(signUpRequest)) {
            log.info("successful membership");
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "일반 로그인", description = "일반 로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        if (userService.signIn(signInRequest)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃", description = "로그아웃",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @PostMapping("/sign-out")
    public ResponseEntity<String> signOut(@RequestHeader(name = "Authorization", required = false) String accessToken,
                                          @RequestHeader(name = "Authorization-refresh", required = false) String refreshToken) {
        User user = jwtService.findUserByToken(accessToken);
        if (userService.signOut(user)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.status(HTTPResponse.SC_UNAUTHORIZED).build();
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @DeleteMapping("")
    public ResponseEntity<String> withdrawal(@RequestHeader(name = "Authorization", required = false) String token) {
        User user = jwtService.findUserByToken(token);
        if (userService.withdrawal(user)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "회원 정보 조회", description = "회원 정보 조회, " +
            "다른 사람 조회 시 닉네임, 랭킹, 퍼센테이지 / 나의 회원 정보 조회 시 추가로 나이, 성별")
    @GetMapping("/{user_id}")
    public ResponseEntity<FindUserInfoResponse> findUserInfo(@RequestHeader(name = "Authorization", required = false) String token,
                                                             @PathVariable("user_id") long userId) {
        User user = null;
        if (token != null) {
            user = jwtService.findUserByToken(token);
        }
        return ResponseEntity.ok()
                .body(userService.findUserInfo(user, userId));
    }

    @Operation(summary = "닉네임 변경", description = "닉네임 변경",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @PatchMapping("/nickname")
    public ResponseEntity<String> updateNickname(@RequestHeader(name = "Authorization", required = false) String token,
                                                 @RequestBody UpdateNicknameRequest updateNicknameRequest) {
        User user = jwtService.findUserByToken(token);
        if (userService.updateNickname(user, updateNicknameRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
