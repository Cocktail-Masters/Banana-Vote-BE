package com.cocktailmasters.backend.common.service;

import com.cocktailmasters.backend.common.contorller.dto.CreateTagsRequest;
import com.cocktailmasters.backend.common.contorller.dto.FindTop10TagsResponse;
import com.cocktailmasters.backend.common.contorller.dto.item.TagDto;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public boolean createTags(CreateTagsRequest createTagsRequest) {
        //TODO: 관리자 검증
        for (String tagName : createTagsRequest.getTags()) {
            if (tagRepository.findByTagName(tagName).isEmpty()) {
                createTagsRequest.getTags().remove(tagName);
            }
        }
        tagRepository.saveAll(createTagsRequest.toTagEntity());
        return true;
    }

    @Transactional
    public FindTop10TagsResponse findTop10Tags() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        return FindTop10TagsResponse.builder()
                .tags(tagRepository.findTop10ByLastModifiedDateBetweenOrderByTagUsedNumber(startDate, endDate)
                        .stream()
                        .map(tag -> TagDto.createTagDto(tag))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public boolean deleteTag(Long tagId) {
        tagRepository.delete(findTagById(tagId));
        return true;
    }

    private Tag findTagById(Long tagId) {
        //TODO: 예외처리 필요
        return tagRepository.findById(tagId)
                .orElseThrow();
    }
}
