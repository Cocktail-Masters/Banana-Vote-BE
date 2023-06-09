package com.cocktailmasters.backend.common.contorller;

import com.cocktailmasters.backend.common.contorller.dto.CreateTagsRequest;
import com.cocktailmasters.backend.common.contorller.dto.DeleteTagsRequest;
import com.cocktailmasters.backend.common.contorller.dto.FindTop10TagsResponse;
import com.cocktailmasters.backend.common.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.cocktailmasters.backend.config.SwaggerConfig.SECURITY_SCHEME_NAME;

@Tag(name = "태그", description = "태그 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "DB에 태그 추가", description = "DB를 태그들 추가, 없는 태그 추가, 있는 태그 무시",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity<String> createTags(@RequestHeader(name = "Authorization", required = false) String token,
                                             @RequestBody CreateTagsRequest createTagsRequest) {
        if (tagService.createTags(createTagsRequest))
            return ResponseEntity.created(null).build();
        else
            return ResponseEntity.noContent().build();
    }

    @Operation(summary = "최근 가장 많이 사용된 태그 10개 반환", description = "1주일 내 가장 많이 사용된 태그 반환")
    @GetMapping("")
    public ResponseEntity<FindTop10TagsResponse> findTop10Tags() {
        return ResponseEntity.ok()
                .body(tagService.findTop10Tags());
    }

    @Operation(summary = "해당 단어가 포함된 모든 태그들 삭제", description = "DB에 있는 태그들 삭제, 없는 태그들은 무시",
            security = {@SecurityRequirement(name = SECURITY_SCHEME_NAME)})
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteTags(@RequestHeader(name = "Authorization", required = false) String token,
                                             @RequestBody DeleteTagsRequest deleteTagsRequest) {
        if (tagService.deleteTags(deleteTagsRequest)) {
            return ResponseEntity.created(null).build();
        }
        return ResponseEntity.badRequest().build();
    }
}
