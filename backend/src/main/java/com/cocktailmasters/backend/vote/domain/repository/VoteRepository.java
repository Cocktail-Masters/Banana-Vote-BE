package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
