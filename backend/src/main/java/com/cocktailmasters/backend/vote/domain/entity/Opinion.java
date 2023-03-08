package com.cocktailmasters.backend.vote.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "opinion_id"))
public class Opinion extends BaseEntity {

    @NotNull
    private String opinionContent;

    @Builder.Default
    private int agreedNumber = 0;

    @Builder.Default
    private int disagreedNumber = 0;

    @Builder.Default
    private int opinionReportedNumber = 0;
}
