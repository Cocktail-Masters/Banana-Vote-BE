package com.cocktailmasters.backend.account.user.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.account.user.controller.dto.*;
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

    @Operation(summary = "나이 변경", description = "나이 변경",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @PatchMapping("/age")
    public ResponseEntity<String> updateAge(@RequestHeader(name = "Authorization", required = false) String token,
                                            @RequestBody @Valid UpdateAgeRequest updateNicknameRequest) {
        User user = jwtService.findUserByToken(token);
        if (userService.updateAge(user, updateNicknameRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "성별 변경", description = "성별 변경, 입력 예제 MALE or FEMALE",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @PatchMapping("/gender")
    public ResponseEntity<String> updateGender(@RequestHeader(name = "Authorization", required = false) String token,
                                               @RequestBody @Valid UpdateGenderRequest updateGenderRequest) {
        User user = jwtService.findUserByToken(token);
        if (userService.updateGender(user, updateGenderRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "관심 태그 조회", description = "관심 태그 조회",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @GetMapping("/tags")
    public ResponseEntity<FindInterestTagsResponse> findInterestTags(@RequestHeader(name = "Authorization", required = false) String token) {
        User user = jwtService.findUserByToken(token);
        return ResponseEntity.ok()
                .body(userService.findInterestTags(user));
    }

    @Operation(summary = "관심 태그 추가", description = "관심 태그 추가",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @PostMapping("/tags")
    public ResponseEntity<String> createInterestTag(@RequestHeader(name = "Authorization", required = false) String token,
                                                    @RequestBody @Valid CreateInterestTagRequest createInterestTagRequest) {
        User user = jwtService.findUserByToken(token);
        String tagName = createInterestTagRequest.getTag().getTagName();
        if (tagName.trim().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        userService.createInterestTag(user, tagName);
        return ResponseEntity.created(null).build();
    }

    @Operation(summary = "관심 태그 삭제", description = "관심 태그 삭제",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'GUEST')")
    @DeleteMapping("/tags")
    public ResponseEntity<String> deleteInterestTag(@RequestHeader(name = "Authorization", required = false) String token,
                                                    @RequestBody @Valid DeleteInterestTagRequest deleteInterestTagRequest) {
        User user = jwtService.findUserByToken(token);
        String tagName = deleteInterestTagRequest.getTag().getTagName();
        if (tagName.trim().isBlank()) {
            return ResponseEntity.noContent().build();
        }
        if (userService.deleteInterestTag(user, tagName)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
