package com.cocktailmasters.backend.picket.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.picket.domain.entity.Picket;

public interface PicketRepository extends JpaRepository<Picket, Long> {
    
    List<Picket> findAllByVoteId(long voteId);

    Optional<Picket> findByVoteIdAndPosition(long voteId, long position);
}
