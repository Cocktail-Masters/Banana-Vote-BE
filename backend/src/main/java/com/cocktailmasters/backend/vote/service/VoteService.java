package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import com.cocktailmasters.backend.vote.controller.dto.CreateVoteRequest;
import com.cocktailmasters.backend.vote.controller.dto.FindVoteDetailResponse;
import com.cocktailmasters.backend.vote.controller.dto.FindVoteOpinionsResponse;
import com.cocktailmasters.backend.vote.controller.dto.item.*;
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
import java.util.stream.Collectors;

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

    @Transactional
    public FindVoteDetailResponse findVoteDetail(Long voteId) {
        Vote vote = findVoteById(voteId);
        voteRepository.save(vote.builder()
                .voteHits(vote.getVoteHits() + 1)
                .build());
        User writer = vote.getUser();
        List<VoteItem> voteItems = vote.getVoteItems();
        return FindVoteDetailResponse.builder()
                .vote(VoteDetailDto.builder()
                        .id(vote.getId())
                        .title(vote.getVoteTitle())
                        .imageUrl(vote.getVoteImageUrl())
                        .content(vote.getVoteContent())
                        .isEvent(vote.isEvent())
                        .isAnonymous(vote.isAnonymous())
                        .isPublic(vote.isPublic())
                        .isClosed(vote.isClosed())
                        .startDate(vote.getCreatedDate())
                        .endDate(vote.getVoteEndDate())
                        .hits(vote.getVoteHits())
                        .votedNumber(vote.getVotedNumber())
                        .opinionNumber(vote.getOpinionNumber())
                        .tags(vote.getVoteTags()
                                .stream()
                                .map(tag -> tag.getTag().getTagName())
                                .collect(Collectors.toList()))
                        .build())
                .writer(WriterDto.builder()
                        .id(writer.getId())
                        .nickname(writer.getNickname())
                        .badgeImageUrl(writer.getEquippedBadgeImageUrl())
                        .build())
                .voteItems(voteItems.stream()
                        .map(item -> VoteItemDto.builder()
                                .itemNumber(item.getVoteItemNumber())
                                .title(item.getVoteItemTitle())
                                .iframeLink(item.getIframeLink())
                                .imageUrl(item.getVoteItemImageUrl())
                                .totalPoints(item.getTotalPoints())
                                .votedNumber(item.getVotedNumber())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public FindVoteOpinionsResponse findVoteOpinions(Long voteId) {
        Vote vote = findVoteById(voteId);
        return FindVoteOpinionsResponse.builder()
                .opinions(vote.getOpinions()
                        .stream()
                        .map(opinion -> OpinionDto.builder()
                                .id(opinion.getId())
                                .writer(WriterDto.builder()
                                        .id(opinion.getUser().getId())
                                        .nickname(opinion.getUser().getNickname())
                                        .badgeImageUrl(opinion.getUser().getEquippedBadgeImageUrl())
                                        .build())
                                .content(opinion.getOpinionContent())
                                .agreedNumber(opinion.getAgreedNumber())
                                .disagreedNumber(opinion.getDisagreedNumber())
                                .createdDate(opinion.getCreatedDate())
                                .build())
                        .collect(Collectors.toList()))
                .build();
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
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundUserException());
        } catch (NotFoundUserException e) {
            throw new RuntimeException(e);
        }
        // TODO: develop pull 후에 예외처리
    }

    private Vote findVoteById(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow();
    }
}
