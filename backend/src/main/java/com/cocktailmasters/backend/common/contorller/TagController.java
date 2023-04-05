package com.cocktailmasters.backend.common.contorller;

import com.cocktailmasters.backend.common.contorller.dto.CreateTagsRequest;
import com.cocktailmasters.backend.common.contorller.dto.FindTop10TagsResponse;
import com.cocktailmasters.backend.common.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "tag", description = "태그 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "DB에 태그 추가", description = "DB를 태그들 추가, 없는 태그 추가, 있는 태그 무시")
    @PostMapping("")
    public ResponseEntity<String> createTags(CreateTagsRequest createTagsRequest) throws Exception {
        //TODO: 관리자 검증
        if (tagService.createTags(createTagsRequest)) {
            return ResponseEntity.created(null).build();
        }
        //TODO: 예외처리
        throw new Exception();
    }

    @Operation(summary = "최근 가장 많이 사용된 태그 10개 반환", description = "1주일 내 가장 많이 사용된 태그 반환")
    @GetMapping("")
    public ResponseEntity<FindTop10TagsResponse> findTop10Tags() {
        return ResponseEntity.ok()
                .body(tagService.findTop10Tags());
    }
}
