package com.cocktailmasters.backend.goods.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cocktailmasters.backend.goods.controller.dto.GoodsRequest;
import com.cocktailmasters.backend.goods.controller.dto.GoodsResponse;
import com.cocktailmasters.backend.goods.controller.dto.UserGoodsResponse;
import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.service.GoodsService;
import com.cocktailmasters.backend.goods.service.UserGoodsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "상품", description = "상품과 관련된 기능 - 조회, 구매")
@RequiredArgsConstructor
@RestController
@RequestMapping("/goods")
public class GoodsController {

    private final GoodsService goodsService;
    private final UserGoodsService userGoodsService;

    @Operation(summary = "상품들의 타입 리스트 반환", description = "상품들의 타입 리스트 반환")
    @GetMapping("/types")
    public ResponseEntity<List<GoodsType>> getGoodsTypes() {
        List<GoodsType> goodsTypes = goodsService.getGoodsTypes();

        if (goodsTypes.size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(goodsTypes);
    }

    @Operation(summary = "상품들의 리스트 반환", description = "type 미 명시 시 전체(뱃지의 경우엔 X), 정렬 기준 default는 1, 기본 페이지는 0, 기본 페이지 사이즈는 10")
    @GetMapping("/list")
    public ResponseEntity<GoodsResponse> getGoods(
            @RequestParam(required = false, name = "type", defaultValue = "-1") String goodsTypeName,
            @RequestParam(required = false, name = "sortby", defaultValue = "1") int sortBy,
            @RequestParam(required = false, name = "page", defaultValue = "0") int page,
            @RequestParam(required = false, name = "size", defaultValue = "10") int pageSize) {
        if (page < 0 || pageSize < 1)
            return ResponseEntity.badRequest().build();

        GoodsType goodsType = goodsService.getGoodsTypeByName(goodsTypeName);
        GoodsResponse goodsResponse = goodsService.getGoodsListWithPage(goodsType, sortBy, page, pageSize);

        if (goodsResponse.getGoodsList().size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(goodsResponse);
    }

    @Operation(summary = "상품을 구매", description = "상품의 수량이 부족할 경우, 구매 일자가 유효하지 않을 경우 에러, 상품이 존재하지 않을 경우에도 에러")
    @PostMapping("/{goodsId}")
    public ResponseEntity<String> buyGoods(@PathVariable long goodsId,
            @RequestParam(required = false, name = "quantity", defaultValue = "1") int quantity) {
        // TODO : 로그인 여부 확인 및 유저 아이디 확인
        long userId = 1;

        if (quantity < 0)
            return ResponseEntity.badRequest().build();

        if (!goodsService.buyGoods(userId, quantity, goodsId))
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저가 가진 상품 목록 조회", description = "유저가 가진 상품 목록 조회")
    @GetMapping("/users")
    public ResponseEntity<List<UserGoodsResponse>> getUsersGoodsList() {
        // TODO : 로그인 및 유저 정보 확인
        long userId = 1;

        List<UserGoodsResponse> userGoods = userGoodsService.getUsersGoods(userId);

        if (userGoods.size() == 0)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.ok().body(userGoods);
    }

    @Operation(summary = "굿즈 생성(관리자용)", description = "굿즈 생성, 타입이 뱃지 일경우엔 X, 뱃지 생성 API 이용하세요")
    @PostMapping
    public ResponseEntity<String> createGoods(@RequestBody @Valid GoodsRequest goodsRequest) {
        // TODO : 관리자 확인

        // date validation
        if (goodsRequest.getStartDate().isAfter(goodsRequest.getEndDate()))
            return ResponseEntity.badRequest().body("invalud date");

        // type check
        if (goodsRequest.getType() == GoodsType.BADGE)
            return ResponseEntity.badRequest().body("use badge api for creating badge");

        if (goodsService.addGoods(goodsRequest))
            return ResponseEntity.created(null).build();
        else
            return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "굿즈 삭제(관리자용)", description = "굿즈 삭제")
    @DeleteMapping
    public ResponseEntity<String> deleteGoods(@PathVariable long goodsId) {
        // TODO : 관리자 확인

        if (goodsService.deleteGoods(goodsId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }

    @Operation(summary = "굿즈 수정(관리자용)", description = "굿즈 수정, 기입하지 않은 정보가 있을 시에 기본값으로 초기화에 주의")
    @PatchMapping
    public ResponseEntity<String> modifyGoods(@PathVariable long goodsId,
            @RequestBody @Valid GoodsRequest goodsRequest) {
        // TODO : 관리자 확인

        // date validation
        if (goodsRequest.getStartDate().isAfter(goodsRequest.getEndDate()))
            return ResponseEntity.badRequest().body("invalud date");

        // type check
        if (goodsRequest.getType() == GoodsType.BADGE)
            return ResponseEntity.badRequest().body("use badge api for creating badge");

        if (goodsService.modifyGoods(goodsId, goodsRequest))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.notFound().build();
    }
}
