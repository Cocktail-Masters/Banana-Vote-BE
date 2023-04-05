package com.cocktailmasters.backend.common.domain.repository;

import com.cocktailmasters.backend.common.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByTagName(String tagName);

    List<Tag> findTop10ByLastModifiedDateBetweenOrderByTagUsedNumber(LocalDate start, LocalDate end);
}
