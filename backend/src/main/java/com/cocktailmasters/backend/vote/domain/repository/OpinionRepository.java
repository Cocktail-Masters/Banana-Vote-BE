package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OpinionRepository extends JpaRepository<Opinion, Long> {

    String countOpinionsQuery = "SELECT COUNT(o,*) " +
            "FROM OPINION o " +
            "WHERE o.vote_id = :voteId " +
            "AND is_active = true";

    Optional<Opinion> findFirstByVoteIdOrderByAgreedNumberDesc(Long voteId);

    @Query(value = "SELECT o.* " +
            "FROM opinion o " +
            "WHERE o.vote_id = :vote_id " +
            "AND is_active = true " +
            "ORDER BY :sort_by",
            countQuery = "countOpinionsQuery",
            nativeQuery = true)
    Page<Opinion> findOpinionsByVoteIdAndOption(@Param("vote_id") Long voteId,
                                                @Param("sort_by") String sortBy,
                                                Pageable pageable);
}
