package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.vote.controller.dto.item.OpinionDto;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateOpinionRequest;
import com.cocktailmasters.backend.vote.controller.dto.opinion.FindOpinionsResponse;
import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import com.cocktailmasters.backend.vote.domain.entity.OpinionSortBy;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
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

    private User findUserById(Long userId) {
        //TODO: 예외처리
        return userRepository.findById(userId)
                .orElseThrow();
    }

    private Vote findVoteById(Long voteId) {
        //TODO: 예외처리
        return voteRepository.findById(voteId)
                .orElseThrow();

    }
}
