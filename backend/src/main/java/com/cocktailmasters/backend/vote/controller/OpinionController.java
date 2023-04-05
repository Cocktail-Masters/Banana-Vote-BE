package com.cocktailmasters.backend.vote.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "opinion", description = "의견 관리")
@RequiredArgsConstructor
@RestController
@RequestMapping("/opinions")
public class OpinionController {
}
