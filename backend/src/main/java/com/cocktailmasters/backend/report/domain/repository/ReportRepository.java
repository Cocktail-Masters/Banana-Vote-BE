package com.cocktailmasters.backend.report.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.report.domain.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByisAllow(boolean isAllow, Pageable pageable);

    long countByisAllow(boolean isAllow);
}
