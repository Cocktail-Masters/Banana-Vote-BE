package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.entity.UserTag;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import com.cocktailmasters.backend.point.service.PointService;
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
    private static final String VOTE_PREDICTION_DESCRIPTION = "Create a prediction";
    private static final String CANCEL_EVENT_VOTING = "Cancel event voting";

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
    public boolean updateEventVote(User user,
                                   CreateVoteRequest createVoteRequest,
                                   long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElse(null);
        if (vote == null) {
            return false;
        }
        vote.getVoteItems()
                .stream()
                .forEach(voteItem -> {
                    List<Prediction> predictions = predictionRepository.findByVoteItemId(voteItem.getId())
                            .orElse(null);
                    if (predictions != null) {
                        predictions.stream()
                                .forEach(prediction -> {
                                    pointService.addPoint(prediction.getPredictionPoints(),
                                            CANCEL_EVENT_VOTING,
                                            prediction.getUser().getId());
                                    prediction.deletePrediction();
                                    predictionRepository.save(prediction);
                                });
                    }
                });
        vote.deleteVote();
        voteRepository.save(vote);
        if (createVote(user, createVoteRequest)) {
            return true;
        }
        return false;
    }

    @Transactional
    public FindVotesResponse findVotes(User user,
                                       String keyword,
                                       boolean isTag,
                                       boolean isClosed,
                                       boolean isEvent,
                                       int sortBy,
                                       Pageable pageable) {
        Page<Vote> votes;
        long totalCount;
        if (keyword == null) keyword = "";
        String sortType = VoteSortBy.valueOfNumber(sortBy);
        if (isTag) {
            votes = voteRepository.findVotesByTagAndOption(keyword, isClosed, isEvent, sortType, pageable);
            totalCount = voteRepository.countVotesByTag(keyword, isClosed, isEvent, sortType);
        } else {
            votes = voteRepository.findVotesByTitleAndOption(keyword, isClosed, isEvent, sortType, pageable);
            totalCount = voteRepository.countVotesByTitle(keyword, isClosed, isEvent, sortType);
        }

        return FindVotesResponse.builder()
                .totalCount(totalCount)
                .votes(votes.getContent().stream()
                        .map(vote -> {
                            Opinion opinion = opinionRepository.findFirstByVoteIdOrderByAgreedNumberDesc(vote.getId())
                                    .orElse(null);
                            OpinionDto bestOpinion = null;
                            Boolean isAgree = null;
                            if (opinion != null) {
                                if (user != null) {
                                    isAgree = agreementRepository.findByUserIdAndOpinionId(user.getId(), opinion.getId())
                                            .orElse(null)
                                            .getIsAgree();
                                }
                                bestOpinion = OpinionDto.createOpinionDto(opinion, isAgree);
                            }
                            return VoteDto.builder()
                                    .vote(VoteDetailDto.createVoteDetailDto(vote))
                                    .writer(WriterDto.createWriterDto(vote.getUser()))
                                    .voteItems(vote.getVoteItems().stream()
                                            .map(voteItem -> VoteItemDto.createVoteItemDto(voteItem))
                                            .collect(Collectors.toList()))
                                    .bestOpinion(bestOpinion)
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
        VoteItem voteItem = findVoteItemById(predictionDto.getVoteItemId());
        if (predictionRepository.findByUserIdAndVoteItemId(user.getId(), voteItem.getId())
                .orElse(null) != null) {
            return false;
        }
        voteItem.updateVotedNumber();
        voteItem.updateTotalPoints(predictionDto.getPoints());
        voteItem.updateBestPoints(predictionDto.getPoints());
        Vote vote = voteItem.getVote();
        vote.updateVotedNumber();
        voteRepository.save(vote);
        predictionRepository.save(Prediction.builder()
                .user(user)
                .voteItem(findVoteItemById(createPredictionRequest.getVote().getVoteItemId()))
                .predictionPoints(createPredictionRequest.getVote().getPoints())
                .build());
        pointService.addPoint(-1 * createPredictionRequest.getVote().getPoints(), VOTE_PREDICTION_DESCRIPTION, user.getId());
        return true;
    }

    @Transactional
    public boolean updatePrediction(User user,
                                    UpdatePredictionRequest updatePredictionRequest) throws Exception {
        PredictionDto predictionDto = updatePredictionRequest.getPrediction();
        VoteItem voteItem = findVoteItemById(predictionDto.getVoteItemId());
        Prediction prediction = predictionRepository.findByUserIdAndVoteItemId(user.getId(), voteItem.getId())
                .orElse(null);
        if (prediction == null) {
            return false;
        }
        Long predictionPoint = predictionDto.getPoints();
        voteItem.updateTotalPoints(predictionPoint);
        voteItem.updateBestPoints(predictionPoint);
        prediction.updatePredictionPoints(predictionPoint);
        voteItemRepository.save(voteItem);
        pointService.addPoint(-1 * updatePredictionRequest.getPrediction().getPoints(), VOTE_PREDICTION_DESCRIPTION, user.getId());
        return true;
    }

    @Transactional
    public boolean deleteVote(Long voteId) {
        Vote vote = voteRepository.findById(voteId)
                .orElse(null);
        if (vote != null) {
            vote.deleteVote();
            voteRepository.saveAndFlush(vote);
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
    public FindInterestVotesResponse findInterestVotes(User user) {
        List<UserTag> userTags = user.getUserTags();
        if (userTags.isEmpty()) {
            return FindInterestVotesResponse.builder().build();
        }
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

//    private void executeVote(long vote)

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
