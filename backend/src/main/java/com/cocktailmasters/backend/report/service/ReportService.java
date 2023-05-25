package com.cocktailmasters.backend.report.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.report.controller.dto.ReportResponse;
import com.cocktailmasters.backend.report.controller.dto.item.ReportItemDto;
import com.cocktailmasters.backend.report.domain.entity.Report;
import com.cocktailmasters.backend.report.domain.repository.ReportRepository;
import com.cocktailmasters.backend.season.controller.dto.RankingResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;

    public boolean reportContent() {

        return false;
    }

    /**
     * get Report list with total page
     * 
     * @param page
     * @param pageSize
     * @return ReportResponse dto
     */
    public ReportResponse getReportListWitPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Report> reportWithPage = reportRepository.findAll(pageable);

        // make list
        List<ReportItemDto> reportList = new ArrayList<>();
        for (Report report : reportWithPage.getContent())
            reportList.add(ReportItemDto.createReportItem(report));

        return new ReportResponse(getReportTotalPages(pageSize), reportList);
    }

    /**
     * get total page of report table
     * 
     * @return total pages
     */
    public int getReportTotalPages(int pageSize) {
        long totalCount = reportRepository.count();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        return totalPages;
    }
}
