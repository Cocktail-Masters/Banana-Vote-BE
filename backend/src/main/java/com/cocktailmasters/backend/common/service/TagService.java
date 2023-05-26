package com.cocktailmasters.backend.common.service;

import com.cocktailmasters.backend.common.contorller.dto.CreateTagsRequest;
import com.cocktailmasters.backend.common.contorller.dto.DeleteTagsRequest;
import com.cocktailmasters.backend.common.contorller.dto.FindTop10TagsResponse;
import com.cocktailmasters.backend.common.contorller.dto.item.TagDto;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    /**
     * create tag (not duplicated with name)
     *
     * @param createTagsRequest
     * @return true or false(not created anything)
     */
    @Transactional
    public boolean createTags(CreateTagsRequest createTagsRequest) {
        List<String> tags = createTagsRequest.getTagsDto().getTags();
        for (int i = 0; i < tags.size(); i++) {
            String tagName = tags.get(i);
            if (tagRepository.findByTagName(tagName).isPresent() || tagName.isBlank()) {
                tags.remove(tagName);
                i--;
            }
        }
        tagRepository.saveAll(createTagsRequest.toTagEntity());
        if (tags.size() == 0)
            return false;
        else
            return true;
    }

    @Transactional
    public FindTop10TagsResponse findTop10Tags() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        return FindTop10TagsResponse.builder()
                .tags(tagRepository.findTop10ByLastModifiedDateBetweenOrderByTagUsedNumber(startDate, endDate)
                        .stream()
                        .map(tag -> TagDto.createTagDto(tag))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public boolean deleteTags(DeleteTagsRequest deleteTagsRequest) {
        List<String> tags = deleteTagsRequest.getTagsDto().getTags();
        if (tags.isEmpty()) {
            return false;
        }
        tags.stream()
                .forEach(tagName -> {
                    tagRepository.deleteAllByTagNameContaining(tagName);
                });
        return true;
    }
}
