package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.vote.controller.dto.item.OpinionDto;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateAgreementRequest;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateOpinionRequest;
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

    public boolean createOpinion(Long userId, CreateOpinionRequest createOpinionRequest) {
        opinionRepository.save(createOpinionRequest.toOpinionEntity(findUserById(userId),
                findVoteById(createOpinionRequest.getVoteId())));
        return true;
    }

    @Transactional
    public FindOpinionsResponse findOpinions(Long voteId,
                                             int sortBy,
                                             Pageable pageable) {
        Page<Opinion> opinions = opinionRepository.findOpinionsByVoteIdAndOption(voteId,
                OpinionSortBy.valueOfNumber(sortBy),
                pageable);
        List<Opinion> bestOpinions = opinionRepository.findTop3ByVoteIdAndAgreedNumberGreaterThanOrderByAgreedNumberDesc(voteId, 9);
        return FindOpinionsResponse.builder()
                .opinions(opinions.stream()
                        .map(opinion -> OpinionDto.createOpinionDto(opinion))
                        .collect(Collectors.toList()))
                .bestIds(bestOpinions.stream()
                        .map(opinion -> opinion.getId())
                        .collect(Collectors.toList()))
                .totalCount(opinionRepository.countOpinionsByVoteId(voteId))
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
            agreementRepository.save(createAgreementRequest.toAgreementEntity(findUserById(userId), findOpinionById(opinionId)));
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

    private Vote findVoteById(Long voteId) {
        //TODO: 예외처리
        return voteRepository.findById(voteId)
                .orElseThrow();
    }
}
