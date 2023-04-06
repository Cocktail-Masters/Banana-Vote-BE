package com.cocktailmasters.backend.vote.domain.repository;

import com.cocktailmasters.backend.vote.domain.entity.Vote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    String countVotesByTitleQuery = "SELECT COUNT(v.*) " +
            "FROM vote v " +
            "WHERE (:keyword IS NULL OR vote_title LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:is_closed IS NULL OR is_closed = :isClosed)";

    String countVotesByTagQuery = "SELECT COUNT(v.*) " +
            "FROM vote v " +
            "INNER JOIN vote_tag vt " +
            "ON v.vote_id = vt.vote_id " +
            "WHERE vt.tag_id IN " +
            "(SELECT tag_id " +
            "FROM tag " +
            "WHERE tag_name (:keyword IS NULL OR tag_name LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:is_closed IS NULL OR is_closed = :isClosed)";

    @Query(value = countVotesByTitleQuery, nativeQuery = true)
    long countVotesByTitle(@Param("keyword") String keyword,
                           @Param("isClosed") boolean isClosed);

    @Query(value = countVotesByTagQuery, nativeQuery = true)
    long countVotesByTag(@Param("keyword") String keyword,
                         @Param("isClosed") boolean isClosed);

    @Query(value = "SELECT v.* " +
            "FROM vote v " +
            "WHERE (:keyword IS NULL OR vote_title LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:is_closed IS NULL OR is_closed = :is_closed) " +
            "ORDER BY :sort_by",
            countQuery = countVotesByTitleQuery,
            nativeQuery = true)
    Page<Vote> findVotesByTitleAndOption(@Param("keyword") String keyword,
                                         @Param("is_closed") boolean isClosed,
                                         @Param("sort_by") String sortBy,
                                         Pageable pageable);

    @Query(value = "SELECT v.* " +
            "FROM vote v " +
            "INNER JOIN vote_tag vt " +
            "ON v.vote_id = vt.vote_id " +
            "WHERE vt.tag_id IN " +
            "(SELECT tag_id " +
            "FROM tag " +
            "WHERE tag_name (:keyword IS NULL OR tag_name LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:is_closed IS NULL OR is_closed = :is_closed) " +
            "ORDER BY :sort_by",
            countQuery = countVotesByTagQuery,
            nativeQuery = true)
    Page<Vote> findVotesByTagAndOption(@Param("keyword") String keyword,
                                       @Param("is_closed") boolean isClosed,
                                       @Param("sort_by") String sortBy,
                                       Pageable pageable);

    List<Vote> findTop5ByIsActiveTrueAndIsClosedFalseOrderByVotedNumberDesc();

    @Query(value = "SELECT v.* " +
            "FROM vote v " +
            "INNER JOIN vote_tag vt " +
            "ON v.vote_id = vt.vote_id " +
            "WHERE vt.tag_id IN " +
            "(SELECT tag_id " +
            "FROM tag " +
            "WHERE tag_name (:keyword IS NULL OR tag_name LIKE CONCAT('%', :keyword, '%')) " +
            "AND is_closed = false " +
            "ORDER BY created_date " +
            "limit 5",
            nativeQuery = true)
    List<Vote> findVoteByUserTag(@Param("keyword") String keyword);
}
