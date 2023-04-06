package com.cocktailmasters.backend.vote.service;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.vote.controller.dto.opinion.CreateOpinionRequest;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.repository.OpinionRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
