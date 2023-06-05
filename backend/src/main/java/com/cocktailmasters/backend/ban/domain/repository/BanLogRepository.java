package com.cocktailmasters.backend.ban.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.ban.domain.entity.BanLog;

public interface BanLogRepository extends JpaRepository<BanLog, Long> {
}
