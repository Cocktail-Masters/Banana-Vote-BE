package com.cocktailmasters.backend.report.domain.entity;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.report.domain.ReportedContentType;
import com.cocktailmasters.backend.report.domain.ReportedReasonType;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "report_id"))
@Entity
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
