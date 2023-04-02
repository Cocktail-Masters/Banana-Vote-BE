package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import com.cocktailmasters.backend.vote.controller.dto.CreateVoteRequest;
import com.cocktailmasters.backend.vote.controller.dto.FindVoteDetailResponse;
import com.cocktailmasters.backend.vote.controller.dto.FindVoteOpinionsResponse;
import com.cocktailmasters.backend.vote.controller.dto.FindVotesResponse;
import com.cocktailmasters.backend.vote.controller.dto.item.*;
import com.cocktailmasters.backend.vote.domain.entity.OrderBy;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import com.cocktailmasters.backend.vote.domain.entity.VoteTag;
import com.cocktailmasters.backend.vote.domain.repository.OpinionRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteItemRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteTagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final OpinionRepository opinionRepository;
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
        User user = findUserById(userId);
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
        voteRepository.save(CreateVoteRequest.toVoteEntity(user, createVoteRequest, voteItems, voteTags));
        return true;
    }

    @Transactional
    public FindVotesResponse findVotes(String keyword,
                                       boolean isTag,
                                       boolean isClosed,
                                       int orderBy,
                                       Pageable pageable) {
        Page<Vote> votes;
        long totalCount;
        if (isTag) {
            votes = voteRepository.findVotesByTagAndOption(keyword,
                    (isClosed ? true : null),
                    OrderBy.valueOfNumber(orderBy),
                    pageable);
            totalCount = voteRepository.countVotesByTag(keyword,
                    (isClosed ? true : null),
                    OrderBy.valueOfNumber(orderBy));
        } else {
            votes = voteRepository.findVotesByTitleAndOption(keyword,
                    (isClosed ? true : null),
                    OrderBy.valueOfNumber(orderBy),
                    pageable);
            totalCount = voteRepository.countVotesByTitle(keyword,
                    (isClosed ? true : null),
                    OrderBy.valueOfNumber(orderBy));
        }
        return FindVotesResponse.builder()
                .totalCount(totalCount)
                .votes(votes.stream()
                        .map(vote -> VoteDto.builder()
                                .vote(VoteDetailDto.createVoteDetailDto(vote))
                                .writer(WriterDto.createWriterDto(vote.getUser()))
                                .voteItems(vote.getVoteItems().stream()
                                        .map(voteItem -> VoteItemDto.createVoteItemDto(voteItem))
                                        .collect(Collectors.toList()))
                                .bestOpinion(OpinionDto.createOpinionDto(opinionRepository.findFirstByVoteIdOrderByAgreedNumberDesc(vote.getId())
                                        .orElse(null)))
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public FindVoteDetailResponse findVoteDetail(Long voteId) {
        Vote vote = findVoteById(voteId);
        voteRepository.save(vote.builder()
                .voteHits(vote.getVoteHits() + 1)
                .build());
        User writer = vote.getUser();
        return FindVoteDetailResponse.builder()
                .vote(VoteDetailDto.createVoteDetailDto(vote))
                .writer(WriterDto.createWriterDto(writer))
                .voteItems(vote.getVoteItems().stream()
                        .map(voteItem -> VoteItemDto.createVoteItemDto(voteItem))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public FindVoteOpinionsResponse findVoteOpinions(Long voteId) {
        Vote vote = findVoteById(voteId);
        return FindVoteOpinionsResponse.builder()
                .opinions(vote.getOpinions()
                        .stream()
                        .map(opinion -> OpinionDto.createOpinionDto(opinion))
                        .collect(Collectors.toList()))
                .build();
    }

    private VoteItem createVoteItem(CreateVoteItemRequest createVoteItemRequest) {
        VoteItem voteItem = createVoteItemRequest.toVoteItemEntity(createVoteItemRequest);
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
