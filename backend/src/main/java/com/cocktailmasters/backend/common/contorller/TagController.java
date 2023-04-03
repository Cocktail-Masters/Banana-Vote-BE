package com.cocktailmasters.backend.common.contorller;

import com.cocktailmasters.backend.common.contorller.dto.CreateTagsRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "tag", description = "태그 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/tags")
public class TagController {

}
