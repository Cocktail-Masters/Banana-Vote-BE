package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {
}
