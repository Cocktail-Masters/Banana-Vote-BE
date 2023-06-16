package com.cocktailmasters.backend.season.controller;

import com.cocktailmasters.backend.account.jwt.service.JwtService;
import com.cocktailmasters.backend.season.controller.dto.RankingResponse;
import com.cocktailmasters.backend.season.service.RankingService;
import com.cocktailmasters.backend.season.service.SeasonService;
import com.cocktailmasters.backend.util.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "랭킹", description = "순위과 관련된 정보 제공")
@RequiredArgsConstructor
@RestController
@RequestMapping("/ranking")
public class RankingController {

    private final RankingService rankingService;
    private final SeasonService seasonService;
    private final JwtService jwtService;

    private final String DEFAULT_PAGE_SIZE = "10";

    @Operation(summary = "랭킹 정보 조회", description = "일치된 시즌의 페이지에 해당하는 랭킹 정보 리스트 반환, 닉네임을 포함 할 경우 페이지 파라미터는 무효되고, 페이지 사이즈에 맞추어 해당 플레이어가 포함된 결과 반환")
    @GetMapping("/{seasonId}")
    public ResponseEntity<RankingResponse> getRanking(@PathVariable(required = false) Optional<Long> seasonId,
                                                      @RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                                      @RequestParam(required = false, name = "size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
                                                      @RequestParam(required = false, name = "nickname") String nickname) {
        long seasonIdNum = seasonId.orElse(seasonService.getLatestSeason().getId());

        if (seasonIdNum == -1) // check season is existed
            throw new CustomException(HttpStatus.NOT_FOUND, "there is no season");
        if (page < 0 || pageSize < 1)
            throw new CustomException(HttpStatus.BAD_REQUEST, "bad page or page size request");

        if (nickname != null && !nickname.isBlank()) {
            page = rankingService.getRankingPageNumberByNickname(seasonIdNum, pageSize, nickname);
            if (page == -1) // nickname not found
                return ResponseEntity.noContent().build();
        }

        RankingResponse rankingResponse = rankingService.getRankingListWithPage(seasonIdNum, page, pageSize);

        if (rankingResponse.getRankingList() == null || rankingResponse.getRankingList().isEmpty())
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(rankingResponse);
    }

    @Operation(summary = "로그인 한 유저의 현재 시즌 랭킹 스코어 조회(로그인 필요)", description = "현재 시즌 랭킹 스코어를 조회, 현재 진행중 시즌없을 경우 no content code", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/score")
    public ResponseEntity<Long> getUserScore(@RequestHeader(name = "Authorization", required = false) String token) {
        long userId = jwtService.findUserByToken(token).getId();

        long userScore = rankingService.getCurrentSeasonUserScore(userId);

        if (userScore == -1)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(userScore);
    }

    @Operation(summary = "로그인 한 유저의 현재 시즌 랭킹 순위 조회(로그인 필요)", description = "현재 시즌 랭킹 순위를 조회, 현재 진행중 시즌없을 경우 no content code", security = {
            @SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("")
    public ResponseEntity<Long> getUserRanking(@RequestHeader(name = "Authorization", required = false) String token) {
        long userId = jwtService.findUserByToken(token).getId();

        long userRanking = rankingService.getCurrentSeasonUserScore(userId);

        if (userRanking == -1)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(userRanking);
    }
}
