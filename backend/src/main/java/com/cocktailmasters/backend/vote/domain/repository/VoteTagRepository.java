package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.VoteTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteTagRepository extends JpaRepository<VoteTag, Long> {
}
