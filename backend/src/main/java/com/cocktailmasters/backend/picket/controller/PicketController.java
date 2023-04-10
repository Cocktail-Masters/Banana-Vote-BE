package com.cocktailmasters.backend.picket.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.picket.controller.dto.PicketConflictedResponse;
import com.cocktailmasters.backend.picket.controller.dto.PicketResponse;
import com.cocktailmasters.backend.picket.controller.dto.item.PicketItem;
import com.cocktailmasters.backend.picket.service.PicketService;
import com.cocktailmasters.backend.picket.controller.dto.PicketRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "피켓(선거 내 유저 광고)", description = "피켓과 관련된 상호작용들")
@RequiredArgsConstructor
@RestController
@RequestMapping("/pickets")
public class PicketController {

    private final PicketService picketService;
    
    @Operation(summary = "투표에 달린 피켓반환",
        description = "해당 투표의 피켓 리스트와 최종 업데이트 시간 반환(ex. 2023-04-07T13:30:00), 피켓이 없을 경우 빈 리스트가 담김")
    @GetMapping("/{voteId}")
    public ResponseEntity<PicketResponse> getPickets(@PathVariable long voteId) {
        List<PicketItem> picketItems = picketService.getPicketsInVote(voteId);
        String lastUpdateTime = picketService.getLastPicketUpdateTime(voteId).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return ResponseEntity.ok().body(new PicketResponse(lastUpdateTime, picketItems));
    }

    @Operation(summary = "피켓 구매",
        description = "피켓을 구매, 이미 자신의 것이거나 서버와의 가격의 차이가 있을 경우 실패 코드 반환")
    @PostMapping("/{voteId}")
    public ResponseEntity<PicketConflictedResponse> buyPicket(@PathVariable long voteId, 
            @RequestBody PicketRequest picketRequest) {
        // TODO : 로그인 확인 로직
        long userId = 1;

        if(picketRequest.getPosition() < 0
            || picketRequest.getPaidPrice() < 0
            || picketRequest.getCurPrice() < 0
            || picketRequest.getCurPrice() >= picketRequest.getPaidPrice()
            || picketRequest.getPosition() > 5)
            return ResponseEntity.badRequest().build();

        long res = picketService.buyPicket(voteId, userId, picketRequest);
        String lastUpdateTime = picketService.getLastPicketUpdateTime(voteId).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if(res == -1)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        else if(res == picketRequest.getPaidPrice())
            return ResponseEntity.ok().body(new PicketConflictedResponse(lastUpdateTime, res));
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new PicketConflictedResponse(lastUpdateTime, res));
    }

    @Operation(summary = "내가 산 피켓 정보 수정",
        description = "피켓 정보를 수정, 자신의 피켓이아닌 경우 에러 코드 반환")
    @PatchMapping("/{voteId}")
    public ResponseEntity<String> modifyPicket(@PathVariable long voteId,
            @RequestBody PicketRequest picketRequest) {
        // TODO : 로그인 확인 로직
        long userId = 1;

        if(picketService.changePicketImage(userId, userId, picketRequest))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "피켓 내리기(관리자용)",
        description = "해당 피켓 삭제, position은 필수 옵션")
    public ResponseEntity<String> removePicket(@PathVariable long voteId,
        @RequestParam(required = true) int position) {
        // TODO : 관리자 and 로그인 확인 로직

        if(picketService.deletePicket(voteId, position))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().build();
    }
}
