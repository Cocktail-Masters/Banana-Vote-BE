package com.cocktailmasters.backend.report.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.common.domain.entity.ReportedContentType;
import com.cocktailmasters.backend.common.domain.entity.ReportedReasonType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "report_id"))
public class Report extends BaseEntity {

    @NotNull
    private ReportedContentType reportedContentType;

    @NotNull
    private Long reportedId;

    @NotNull
    private ReportedReasonType reportedReasonType;

    private String ReportDetail;

    @Builder.Default
    private boolean isAllow = false;
}
