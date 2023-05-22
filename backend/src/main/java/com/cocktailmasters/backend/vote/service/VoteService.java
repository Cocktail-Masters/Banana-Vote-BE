package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.entity.UserTag;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import com.cocktailmasters.backend.point.service.PointService;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import com.cocktailmasters.backend.vote.controller.dto.item.*;
import com.cocktailmasters.backend.vote.controller.dto.vote.*;
import com.cocktailmasters.backend.vote.domain.entity.*;
import com.cocktailmasters.backend.vote.domain.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VoteService {

    private static final long CREATE_VOTE_POINTS = 20;
    private static final long CREATE_VOTE_POINTS_PER_DAY = 5;
    private static final String CREATE_VOTE_POINT_LOG_DESCRIPTION = "Create a vote";

    private final OpinionRepository opinionRepository;
    private final PredictionRepository predictionRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final VoteItemRepository voteItemRepository;
    private final AgreementRepository agreementRepository;

    private final PointService pointService;

    @Transactional
    public boolean createVote(User user,
                              CreateVoteRequest createVoteRequest) {
        Vote vote = createVoteRequest.toVoteEntity(user);
        createVoteRequest.getVoteItems()
                .forEach(voteItem -> {
                    VoteItem voteItem1 = createVoteItem(voteItem, vote);
                    vote.addVoteItem(voteItem1);
                });
        createVoteRequest.getTags().
                forEach(tagName -> {
                    Tag tag = findTagByTagName(tagName);
                    tag.countTagUsedNumber();
                    tag.updateLastModifiedDate();
                    tagRepository.save(tag);
                    VoteTag voteTag = createVoteTag(tag, vote);
                    vote.addVoteTag(voteTag);
                });
        voteRepository.save(vote);

        int votePeriod = Period.between(LocalDate.now(), createVoteRequest.getVoteEndDate().toLocalDate())
                .getDays();
        long pointAmount;
        if (votePeriod == 1) {
            pointAmount = CREATE_VOTE_POINTS;
            pointService.addPoint(pointAmount, CREATE_VOTE_POINT_LOG_DESCRIPTION, user.getId());
        } else {
            pointAmount = CREATE_VOTE_POINTS + (CREATE_VOTE_POINTS_PER_DAY * (votePeriod - 1));
            pointService.addPoint(pointAmount, CREATE_VOTE_POINT_LOG_DESCRIPTION, user.getId());
        }
        return true;
    }

    @Transactional
    public FindVotesResponse findVotes(User user,
                                       String keyword,
                                       boolean isTag,
                                       Boolean isClosed,
                                       int sortBy,
                                       Pageable pageable) {
        Page<Vote> votes;
        long totalCount;
        if (isTag) {
            votes = voteRepository.findVotesByTagAndOption(keyword,
                    (isClosed != null ? isClosed : null),
                    VoteSortBy.valueOfNumber(sortBy),
                    pageable);
            totalCount = voteRepository.countVotesByTag(keyword,
                    (isClosed != null ? isClosed : null));
        } else {
            votes = voteRepository.findVotesByTitleAndOption(keyword,
                    (isClosed != null ? isClosed : null),
                    VoteSortBy.valueOfNumber(sortBy),
                    pageable);
            totalCount = voteRepository.countVotesByTitle(keyword,
                    (isClosed != null ? isClosed : null));
        }
        return FindVotesResponse.builder()
                .totalCount(totalCount)
                .votes(votes.stream()
                        .map(vote -> {
                            Opinion opinion = opinionRepository.findFirstByVoteIdOrderByAgreedNumberDesc(vote.getId())
                                    .orElse(null);
                            Agreement agreement = agreementRepository.findByUserIdAndOpinionId(user.getId(), opinion.getId())
                                    .orElse(null);
                            return VoteDto.builder()
                                    .vote(VoteDetailDto.createVoteDetailDto(vote))
                                    .writer(WriterDto.createWriterDto(vote.getUser()))
                                    .voteItems(vote.getVoteItems().stream()
                                            .map(voteItem -> VoteItemDto.createVoteItemDto(voteItem))
                                            .collect(Collectors.toList()))
                                    .bestOpinion(OpinionDto.createOpinionDto(opinion, agreement.getIsAgree()))
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public FindVoteDetailResponse findVoteDetail(Long voteId) {
        Vote vote = findVoteById(voteId);
        vote.updateVoteHits();
        voteRepository.save(vote);
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
    public FindVoteParticipationResponse findVoteParticipation(User user,
                                                               Long voteId) {
        Prediction prediction;
        List<VoteItem> voteItems = findVoteById(voteId).getVoteItems();
        for (VoteItem voteItem : voteItems) {
            prediction = predictionRepository.findByUserIdAndVoteItemId(user.getId(), voteItem.getId())
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
    public boolean createPrediction(User user,
                                    CreatePredictionRequest createPredictionRequest) throws Exception {
        PredictionDto predictionDto = createPredictionRequest.getVote();
        //TODO: 포인트가 모자랄 시 예외 처리 적용
        //TODO: 포인트 사용 시 로그 생성
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

    private VoteItem createVoteItem(VoteItemCreateDto createVoteItemRequest, Vote vote) {
        VoteItem voteItem = createVoteItemRequest.toVoteItemEntity(createVoteItemRequest, vote);
        return voteItem;
    }

    private VoteTag createVoteTag(Tag tag, Vote vote) {
        VoteTag voteTag = VoteTag.builder()
                .tag(tag)
                .vote(vote)
                .build();
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
