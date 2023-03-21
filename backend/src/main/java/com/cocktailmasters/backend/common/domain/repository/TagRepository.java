package com.cocktailmasters.backend.common.domain.repository;

import com.cocktailmasters.backend.common.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
