package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import com.cocktailmasters.backend.vote.controller.dto.CreateVoteRequest;
import com.cocktailmasters.backend.vote.controller.dto.item.CreateVoteItemRequest;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.cocktailmasters.backend.vote.domain.entity.VoteTag;
import com.cocktailmasters.backend.vote.domain.repository.VoteItemRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteTagRepository voteTagRepository;

    @Transactional
    public boolean createVote(Long userId, CreateVoteRequest createVoteRequest) {
        // TODO: controller에서 사용자 검사 후, service에서 사용자 객체 받기
        // TODO: request 검사
        // TODO: 작성자 포인트 감소
        // User user = findUserById(userId);
        List<VoteItem> voteItems = new ArrayList<>();
        List<VoteTag> voteTags = new ArrayList<>();
        createVoteRequest.getVoteItems()
                .forEach(voteItem -> {
                    voteItems.add(createVoteItem(voteItem));
                });
        createVoteRequest.getTags().
                forEach(tagName -> {
                    Tag tag = findTagByTagName(tagName);
                    voteTags.add(createVoteTag(tag));
                });
        voteRepository.save(Vote.builder()
                // .user(user)
                .voteTitle(createVoteRequest.getVoteTitle())
                .voteContent(createVoteRequest.getVoteContent())
                .voteImageUrl(createVoteRequest.getVoteImageUrl())
                .voteThumbnailUrl(createVoteRequest.getVoteThumbnailUrl())
                .voteEndDate(createVoteRequest.getVoteEndDate())
                .isAnonymous(createVoteRequest.getIsAnonymous())
                .isPublic(createVoteRequest.getIsPublic())
                .voteItems(voteItems)
                .voteTags(voteTags)
                .build());
        return true;
    }

    private VoteItem createVoteItem(CreateVoteItemRequest createVoteItemRequest) {
        VoteItem voteItem = VoteItem.builder()
                .voteItemNumber(createVoteItemRequest.getItemNumber())
                .voteItemTitle(createVoteItemRequest.getTitle())
                .voteItemImageUrl(createVoteItemRequest.getImageUrl())
                .iframeLink(createVoteItemRequest.getIframeLink())
                .build();
        voteItemRepository.save(voteItem);
        return voteItem;
    }

    private Tag findTagByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if (tag == null) {
            tag = Tag.builder()
                    .tagName(tagName)
                    .build();
        }
        tag.countTagUsedNumber();
        tagRepository.save(tag);
        return tag;
    }

    private VoteTag createVoteTag(Tag tag) {
        VoteTag voteTag = VoteTag.builder()
                .tag(tag)
                .build();
        voteTagRepository.save(voteTag);
        return voteTag;
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow();
        // TODO: develop pull 후에 예외처리
    }
}
