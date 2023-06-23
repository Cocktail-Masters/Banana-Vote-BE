package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    String countVotesByTitleQuery = "SELECT COUNT(*) FROM vote v " +
            "WHERE v.vote_title LIKE CONCAT('%', :keyword, '%') " +
            "AND v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true";

    String countVotesByTagQuery = "SELECT COUNT(*) FROM vote v " +
            "INNER JOIN " +
            "(SELECT vt.* FROM vote_tag vt " +
            "INNER JOIN tag t " +
            "ON vt.tag_tag_id = t.tag_id " +
            "WHERE t.tag_name LIKE CONCAT('%', :keyword, '%')) vtt " +
            "ON v.vote_id = vtt.vote_vote_id " +
            "WHERE v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true";

    String countVotesIsClosedByTitleQuery = "SELECT COUNT(*) FROM vote v " +
            "WHERE v.vote_title LIKE CONCAT('%', :keyword, '%') " +
            "AND v.is_closed = false " +
            "AND v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true";

    String countVotesIsClosedByTagQuery = "SELECT COUNT(*) FROM vote v " +
            "INNER JOIN " +
            "(SELECT vt.* FROM vote_tag vt " +
            "INNER JOIN tag t " +
            "ON vt.tag_tag_id = t.tag_id " +
            "WHERE t.tag_name LIKE CONCAT('%', :keyword, '%')) vtt " +
            "ON v.vote_id = vtt.vote_vote_id " +
            "WHERE v.is_closed = false " +
            "AND v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true";

    @Query(value = countVotesByTitleQuery, nativeQuery = true)
    long countVotesByTitle(@Param("keyword") String keyword,
                           @Param("is_event") boolean isEvent);

    @Query(value = countVotesByTagQuery, nativeQuery = true)
    long countVotesByTag(@Param("keyword") String keyword,
                         @Param("is_event") boolean isEvent);

    @Query(value = countVotesIsClosedByTitleQuery, nativeQuery = true)
    long countVotesIsClosedByTitle(@Param("keyword") String keyword,
                                   @Param("is_event") boolean isEvent);

    @Query(value = countVotesIsClosedByTagQuery, nativeQuery = true)
    long countVotesIsClosedByTag(@Param("keyword") String keyword,
                                 @Param("is_event") boolean isEvent);

    @Query(value = "SELECT v.* FROM vote v " +
            "WHERE v.vote_title LIKE CONCAT('%', :keyword, '%') " +
            "AND v.is_closed = false " +
            "AND v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true",
            countQuery = countVotesIsClosedByTitleQuery,
            nativeQuery = true)
    Page<Vote> findVotesIsClosedByTitleAndOption(@Param("keyword") String keyword,
                                                 @Param("is_event") boolean isEvent,
                                                 Pageable pageable);

    @Query(value = "SELECT v.* FROM vote v " +
            "INNER JOIN " +
            "(SELECT vt.* FROM vote_tag vt " +
            "INNER JOIN tag t " +
            "ON vt.tag_tag_id = t.tag_id " +
            "WHERE t.tag_name LIKE CONCAT('%', :keyword, '%')) vtt " +
            "ON v.vote_id = vtt.vote_vote_id " +
            "WHERE v.is_closed = false " +
            "AND v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true",
            countQuery = countVotesIsClosedByTagQuery,
            nativeQuery = true)
    Page<Vote> findVotesIsClosedByTagAndOption(@Param("keyword") String keyword,
                                               @Param("is_event") boolean isEvent,
                                               Pageable pageable);

    @Query(value = "SELECT v.* FROM vote v " +
            "WHERE v.vote_title LIKE CONCAT('%', :keyword, '%') " +
            "AND v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true",
            countQuery = countVotesByTitleQuery,
            nativeQuery = true)
    Page<Vote> findVotesByTitleAndOption(@Param("keyword") String keyword,
                                         @Param("is_event") boolean isEvent,
                                         Pageable pageable);

    @Query(value = "SELECT v.* FROM vote v " +
            "INNER JOIN " +
            "(SELECT vt.* FROM vote_tag vt " +
            "INNER JOIN tag t " +
            "ON vt.tag_tag_id = t.tag_id " +
            "WHERE t.tag_name LIKE CONCAT('%', :keyword, '%')) vtt " +
            "ON v.vote_id = vtt.vote_vote_id " +
            "WHERE v.is_event = :is_event " +
            "AND v.is_public = true " +
            "AND v.is_active = true",
            countQuery = countVotesByTagQuery,
            nativeQuery = true)
    Page<Vote> findVotesByTagAndOption(@Param("keyword") String keyword,
                                       @Param("is_event") boolean isEvent,
                                       Pageable pageable);

    List<Vote> findTop5ByIsActiveTrueAndIsClosedFalseOrderByVotedNumberDesc();

    @Query(value = "SELECT v.* " +
            "FROM vote v " +
            "INNER JOIN vote_tag vt " +
            "ON v.vote_id = vt.vote_id " +
            "WHERE vt.tag_id IN " +
            "(SELECT tag_id " +
            "FROM tag " +
            "WHERE tag_name LIKE CONCAT('%', :keyword, '%')) " +
            "AND is_closed = false " +
            "AND v.is_active = true " +
            "ORDER BY created_date " +
            "limit 5",
            nativeQuery = true)
    List<Vote> findVoteByUserTag(@Param("keyword") String keyword);

    Optional<Vote> findByIdAndIsActiveTrue(Long voteId);
}
