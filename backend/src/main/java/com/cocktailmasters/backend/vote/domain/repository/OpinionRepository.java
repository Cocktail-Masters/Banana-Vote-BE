package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {

    Optional<Opinion> findFirstByVoteIdOrderByAgreedNumberDesc(Long VoteId);
}
