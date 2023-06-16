package com.cocktailmasters.backend.megaphone.domain.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.megaphone.domain.entity.Megaphone;

public interface MegaphoneRepository extends JpaRepository<Megaphone, Long> {
    public List<Megaphone> findByMegaphoneEndDateTimeAfter(LocalDateTime date);

    public Long countByUserId(long userId);
}
