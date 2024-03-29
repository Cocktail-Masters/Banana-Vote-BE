package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {

    String countOpinionsQuery = "SELECT COUNT(*) " +
            "FROM opinion o " +
            "WHERE o.vote_vote_id = :vote_id " +
            "AND o.is_active = true";

    Optional<Opinion> findFirstByVoteIdOrderByAgreedNumberDesc(Long voteId);

    @Query(value = "SELECT o.* " +
            "FROM opinion o " +
            "WHERE o.vote_vote_id = :vote_id " +
            "AND o.is_active = true",
            countQuery = countOpinionsQuery,
            nativeQuery = true)
    Page<Opinion> findOpinionsByVoteIdAndOption(@Param("vote_id") Long voteId,
                                                Pageable pageable);

    List<Opinion> findTop3ByVoteIdAndAgreedNumberGreaterThanOrderByAgreedNumberDesc(Long voteId, int agreedNumber);

    Optional<Opinion> findByIdAndIsActiveTrue(Long opinionId);

    Optional<Opinion> findByIdAndUserIdAndIsActiveTrue(Long opinionId, Long userId);
}
