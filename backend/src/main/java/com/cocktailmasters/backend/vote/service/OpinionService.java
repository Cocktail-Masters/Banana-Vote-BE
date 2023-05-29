package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.vote.controller.dto.item.OpinionDto;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateAgreementRequest;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateOpinionRequest;
import com.cocktailmasters.backend.vote.controller.dto.opinion.FindOpinionNumberResponse;
import com.cocktailmasters.backend.vote.controller.dto.opinion.FindOpinionsResponse;
import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import com.cocktailmasters.backend.vote.domain.entity.OpinionSortBy;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.repository.AgreementRepository;
import com.cocktailmasters.backend.vote.domain.repository.OpinionRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OpinionService {

    private final AgreementRepository agreementRepository;
    private final OpinionRepository opinionRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public boolean createOpinion(User user, CreateOpinionRequest createOpinionRequest) {
        if (createOpinionRequest.getContent().trim().isEmpty()) {
            return false;
        }
        Vote vote = findVoteById(createOpinionRequest.getVoteId());
        if (vote == null) {
            return false;
        }
        opinionRepository.save(createOpinionRequest.toOpinionEntity(user, vote));
        return true;
    }

    @Transactional
    public FindOpinionsResponse findOpinions(Long userId, Long voteId, int sortBy, Pageable pageable) {
        Page<Opinion> opinions = opinionRepository.findOpinionsByVoteIdAndOption(voteId,
                OpinionSortBy.valueOfNumber(sortBy),
                pageable);
        List<Opinion> bestOpinions = opinionRepository.findTop3ByVoteIdAndAgreedNumberGreaterThanOrderByAgreedNumberDesc(voteId, 9);
        return FindOpinionsResponse.builder()
                .opinions(opinions.stream()
                        .map(opinion -> {
                            if (userId != null) {
                                return OpinionDto.createOpinionDto(opinion, agreementRepository.findByUserIdAndOpinionId(userId, opinion.getId())
                                        .get()
                                        .getIsAgree());
                            }
                            return OpinionDto.createOpinionDto(opinion, null);
                        }).collect(Collectors.toList()))
                .bestIds(bestOpinions.stream()
                        .map(opinion -> opinion.getId())
                        .collect(Collectors.toList()))
                .opinionNumber(findOpinionNumberByVoteId(voteId))
                .build();
    }

    @Transactional
    public FindOpinionNumberResponse findOpinionNumber(Long voteId) {
        return FindOpinionNumberResponse.builder()
                .opinionNumber(findOpinionNumberByVoteId(voteId))
                .build();
    }

    @Transactional
    public boolean deleteOpinion(Long userId, Long opinionId) {
        Optional<Opinion> opinion = opinionRepository.findByIdAndUserId(opinionId, userId);
        if (opinion.isPresent()) {
            opinionRepository.delete(opinion.get());
            return true;
        }
        return false;
    }

    @Transactional
    public boolean createAgreement(Long userId, Long opinionId, CreateAgreementRequest createAgreementRequest) {
        if (agreementRepository.findByUserIdAndOpinionId(userId, opinionId).isEmpty()) {
            Opinion opinion = findOpinionById(opinionId);
            agreementRepository.save(createAgreementRequest.toAgreementEntity(findUserById(userId), opinion));
            if (createAgreementRequest.getIsAgree()) {
                opinion.agreeOpinion();
            } else {
                opinion.disagreeOpinion();
            }
            opinionRepository.save(opinion);
            return true;
        }
        return false;
    }

    private User findUserById(Long userId) {
        //TODO: 예외처리
        return userRepository.findById(userId)
                .orElseThrow();
    }

    private Opinion findOpinionById(Long opinionId) {
        //TODO: 예외처리
        return opinionRepository.findById(opinionId)
                .orElseThrow();
    }

    private int findOpinionNumberByVoteId(Long voteId) {
        return opinionRepository.countOpinionsByVoteId(voteId);
    }

    private Vote findVoteById(Long voteId) {
        //TODO: 예외처리
        return voteRepository.findByIdAndIsActiveTrue(voteId)
                .orElse(null);
    }
}
