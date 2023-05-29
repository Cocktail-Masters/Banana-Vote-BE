package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.user.domain.entity.Role;
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
        Opinion opinion = createOpinionRequest.toOpinionEntity(user, vote);
        vote.updateOpinionNumber();
        vote.addOpinion(opinion);
        voteRepository.save(vote);
        return true;
    }

    @Transactional
    public FindOpinionsResponse findOpinions(User user, Long voteId, int sortBy, Pageable pageable) {
        Page<Opinion> opinions = opinionRepository.findOpinionsByVoteIdAndOption(voteId,
                OpinionSortBy.valueOfNumber(sortBy),
                pageable);
        List<Opinion> bestOpinions = opinionRepository.findTop3ByVoteIdAndAgreedNumberGreaterThanOrderByAgreedNumberDesc(voteId, 9);
        return FindOpinionsResponse.builder()
                .opinions(opinions.stream()
                        .map(opinion -> {
                            if (user != null) {
                                return OpinionDto.createOpinionDto(opinion, agreementRepository.findByUserIdAndOpinionId(user.getId(), opinion.getId())
                                        .get()
                                        .getIsAgree());
                            }
                            return OpinionDto.createOpinionDto(opinion, null);
                        }).collect(Collectors.toList()))
                .bestIds(bestOpinions.stream()
                        .map(opinion -> opinion.getId())
                        .collect(Collectors.toList()))
                .opinionNumber(findVoteById(voteId).getOpinionNumber())
                .build();
    }

    @Transactional
    public FindOpinionNumberResponse findOpinionNumber(Long voteId) {
        return FindOpinionNumberResponse.builder()
                .opinionNumber(findVoteById(voteId).getOpinionNumber())
                .build();
    }

    @Transactional
    public boolean deleteOpinion(User user, Long opinionId) {
        Opinion opinion;
        if (user.getRole() == Role.ADMIN) {
            opinion = opinionRepository.findByIdAndIsActiveTrue(opinionId)
                    .orElse(null);
        } else {
            opinion = opinionRepository.findByIdAndUserIdAndIsActiveTrue(opinionId, user.getId())
                    .orElse(null);
        }
        if (opinion != null) {
            opinion.deleteOpinion();
            opinionRepository.save(opinion);
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
        return userRepository.findById(userId)
                .orElse(null);
    }

    private Vote findVoteById(Long voteId) {
        return voteRepository.findByIdAndIsActiveTrue(voteId)
                .orElse(null);
    }

    private Opinion findOpinionById(Long opinionId) {
        return opinionRepository.findById(opinionId)
                .orElse(null);
    }
}
