package com.cocktailmasters.backend.common.service;

import com.cocktailmasters.backend.common.contorller.dto.CreateTagsRequest;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

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
}
