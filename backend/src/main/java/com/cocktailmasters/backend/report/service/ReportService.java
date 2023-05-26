package com.cocktailmasters.backend.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.megaphone.domain.repository.MegaphoneRepository;
import com.cocktailmasters.backend.picket.domain.repository.PicketRepository;
import com.cocktailmasters.backend.report.controller.dto.ReportRequest;
import com.cocktailmasters.backend.report.controller.dto.ReportResponse;
import com.cocktailmasters.backend.report.controller.dto.item.ReportItemDto;
import com.cocktailmasters.backend.report.domain.ReportedContentType;
import com.cocktailmasters.backend.report.domain.entity.Report;
import com.cocktailmasters.backend.report.domain.repository.ReportRepository;
import com.cocktailmasters.backend.vote.domain.repository.OpinionRepository;
import com.cocktailmasters.backend.vote.domain.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final VoteRepository voteRepository;
    private final OpinionRepository opinionRepository;
    private final PicketRepository picketRepository;
    private final MegaphoneRepository megaphoneRepository;
    private final UserRepository userRepository;

    /**
     * Report the content
     * 
     * @param reportRequest
     * @return true or false(not found)
     */
    @Transactional
    public boolean reportContent(ReportRequest reportRequest, long userId) {
        ReportedContentType type = reportRequest.getReportedContentType();
        long contentId = reportRequest.getReportedContentId();

        // check content is existed
        switch (type) {
            case VOTE:
                if (!voteRepository.findById(contentId).isPresent())
                    return false;
                break;
            case OPINION:
                if (opinionRepository.findById(contentId).isPresent())
                    return false;
                break;
            case PICKET:
                if (picketRepository.findById(contentId).isPresent())
                    return false;
                break;
            case MEGAPHONE:
                if (megaphoneRepository.findById(contentId).isPresent())
                    return false;
                break;
        }

        Report report = Report.builder().reportedContentType(type)
                .reportedContentId(contentId)
                .reportedReasonType(reportRequest.getReportedReasonType())
                .ReportedReasonDetail(reportRequest.getReportDetail())
                .user(userRepository.findById(userId).get())
                .build();

        reportRepository.save(report);
        return true;
    }

    /**
     * check report (only turning flag on possiable)
     * 
     * @param reportId
     * @return true or false(report not found)
     */
    @Transactional
    public boolean checkReport(long reportId) {
        Optional<Report> report = reportRepository.findById(reportId);

        if (!report.isPresent())
            return false;

        report.get().checkReport();
        reportRepository.save(report.get());
        return true;
    }

    /**
     * get Report list with total page
     * 
     * @param page
     * @param pageSize
     * @return ReportResponse dto
     */
    public ReportResponse getReportListWitPage(int checkOption, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Report> reportWithPage;

        switch (checkOption) {
            case 1: // check true
                reportWithPage = reportRepository.findByisAllow(true, pageable);
                break;
            case 2: // check false
                reportWithPage = reportRepository.findByisAllow(false, pageable);
                break;
            default:
                reportWithPage = reportRepository.findAll(pageable);
                break;
        }

        // make list
        List<ReportItemDto> reportList = new ArrayList<>();
        for (Report report : reportWithPage.getContent())
            reportList.add(ReportItemDto.createReportItem(report));

        return new ReportResponse(getReportTotalPages(checkOption, pageSize), reportList);
    }

    /**
     * get total page of report table
     * 
     * @return total pages
     */
    public int getReportTotalPages(int checkOption, int pageSize) {
        long totalCount = reportRepository.count();

        if (checkOption == 1) // check true
            totalCount = reportRepository.countByisAllow(true);
        else if (checkOption == 2) // check false
            totalCount = reportRepository.countByisAllow(false);

        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        return totalPages;
    }
}
