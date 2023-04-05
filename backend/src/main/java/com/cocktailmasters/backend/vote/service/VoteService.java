package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.entity.UserTag;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import com.cocktailmasters.backend.vote.controller.dto.*;
import com.cocktailmasters.backend.vote.controller.dto.item.*;
import com.cocktailmasters.backend.vote.domain.entity.*;
import com.cocktailmasters.backend.vote.domain.repository.*;
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
    private final PredictionRepository predictionRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final VoteTagRepository voteTagRepository;

    @Transactional
    public boolean createVote(Long userId,
                              CreateVoteRequest createVoteRequest) {
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
                    tag.countTagUsedNumber();
                    tag.updateLastModifiedDate();
                    tagRepository.save(tag);
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

    @Transactional
    public FindVoteParticipationResponse findVoteParticipation(Long userId,
                                                               Long voteId) {
        Prediction prediction;
        List<VoteItem> voteItems = findVoteById(voteId).getVoteItems();
        for (VoteItem voteItem : voteItems) {
            prediction = predictionRepository.findByUserIdAndVoteItemId(userId, voteItem.getId())
                    .orElse(null);
            if (prediction != null) {
                return FindVoteParticipationResponse.builder()
                        .isParticipation(true)
                        .voteItemId(voteItem.getId())
                        .voteNumber(voteItem.getVoteItemNumber())
                        .point(prediction.getPredictionPoints())
                        .build();
            }
        }
        return FindVoteParticipationResponse.builder()
                .isParticipation(false)
                .build();
    }

    @Transactional
    public boolean createPrediction(Long userId,
                                    CreatePredictionRequest createPredictionRequest) throws Exception {
        PredictionDto predictionDto = createPredictionRequest.getVote();
        //TODO: 포인트가 모자랄 시 예외 처리 적용
        //TODO: 포인트 사용 시 로그 생성
        User user = findUserById(userId);
        user.usePoints(predictionDto.getPoints());
        userRepository.save(user);
        VoteItem voteItem = findVoteItemById(predictionDto.getVoteItemId());
        voteItem.updateVotedNumber();
        voteItem.updateTotalPoints(predictionDto.getPoints());
        voteItem.updateBestPoints(predictionDto.getPoints());
        voteItemRepository.save(voteItem);
        Vote vote = voteItem.getVote();
        vote.updateVotedNumber();
        voteRepository.save(vote);
        predictionRepository.save(Prediction.builder()
                .user(user)
                .voteItem(findVoteItemById(createPredictionRequest.getVote().getVoteItemId()))
                .predictionPoints(createPredictionRequest.getVote().getPoints())
                .build());
        return true;
    }

    @Transactional
    public boolean updatePrediction(Long userId,
                                    UpdatePredictionRequest updatePredictionRequest) throws Exception {
        PredictionDto predictionDto = updatePredictionRequest.getPrediction();
        //TODO: 포인트가 모자랄 시 예외 처리 적용
        //TODO: 포인트 사용 시 로그 생성
        User user = findUserById(userId);
        user.usePoints(predictionDto.getPoints());
        userRepository.save(user);
        VoteItem voteItem = findVoteItemById(predictionDto.getVoteItemId());
        voteItem.updateTotalPoints(predictionDto.getPoints());
        voteItem.updateBestPoints(predictionDto.getPoints());
        voteItemRepository.save(voteItem);
        //TODO: 투표한 적이 없을 경우 예외처리
        Prediction prediction = predictionRepository.findByUserIdAndVoteItemId(userId, predictionDto.getVoteItemId())
                .orElseThrow();
        predictionRepository.save(prediction.builder()
                .predictionPoints(updatePredictionRequest.getPrediction().getPoints())
                .build());
        return true;
    }

    @Transactional
    public boolean deleteVote(Long userId,
                              Long voteId) {
        Vote vote = findVoteById(voteId);
        if (vote.getUser().getId() == userId) {
            voteRepository.delete(vote);
            return true;
        }
        return false;
    }

    @Transactional
    public FindPopularVotesResponse findPopularVotes() {
        List<Vote> votes = voteRepository.findTop5ByIsActiveTrueAndIsClosedFalseOrderByVotedNumberDesc();
        return FindPopularVotesResponse.builder()
                .votes(votes.stream()
                        .map(vote -> SimpleVoteDto.createSimpleVoteDto(vote))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public FindInterestVotesResponse findInterestVotes(Long userId) {
        List<UserTag> userTags = findUserById(userId).getUserTags();
        // TODO: 관심 태그 없을 시 예외처리
        if (userTags.isEmpty()) return FindInterestVotesResponse.builder().build();
        List<Vote> votes = new ArrayList<>();
        for (UserTag userTag : userTags) {
            votes.addAll(voteRepository.findVoteByUserTag(userTag.getTag().getTagName()));
        }
        return FindInterestVotesResponse.builder()
                .votes(votes.stream()
                        .map(vote -> SimpleVoteDto.createSimpleVoteDto(vote))
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public FindPredictionsResponse findPredictions(Long voteId) {
        Vote vote = findVoteById(voteId);
        return FindPredictionsResponse.builder()
                .predictions(vote.getVoteItems()
                        .stream()
                        .map(voteItem -> PredictionItemDto.createPredictionItemDto(voteItem))
                        .collect(Collectors.toList()))
                .build();
    }

    private VoteItem createVoteItem(VoteItemCreateDto createVoteItemRequest) {
        VoteItem voteItem = createVoteItemRequest.toVoteItemEntity(createVoteItemRequest);
        voteItemRepository.save(voteItem);
        return voteItem;
    }

    private VoteTag createVoteTag(Tag tag) {
        VoteTag voteTag = VoteTag.builder()
                .tag(tag)
                .build();
        voteTagRepository.save(voteTag);
        return voteTag;
    }

    private User findUserById(Long userId) {
        // TODO: 예외처리
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundUserException());
        } catch (NotFoundUserException e) {
            throw new RuntimeException(e);
        }
    }

    private Vote findVoteById(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow();
    }

    private VoteItem findVoteItemById(Long voteItemId) {
        //TODO: 예외처리
        return voteItemRepository.findById(voteItemId)
                .orElseThrow();
    }

    private Tag findTagByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName)
                .orElse(Tag.builder()
                        .tagName(tagName)
                        .build());
        return tag;
    }
}
