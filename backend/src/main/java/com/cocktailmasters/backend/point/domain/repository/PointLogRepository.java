package com.cocktailmasters.backend.point.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.point.domain.entity.PointLog;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    List<PointLog> findAllByUserId(long userId);
}
