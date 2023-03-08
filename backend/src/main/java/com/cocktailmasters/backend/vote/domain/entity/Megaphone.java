package com.cocktailmasters.backend.vote.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "megaphone_id"))
public class Megaphone extends BaseEntity {

    @NotNull
    private String megaphoneContent;

    private String voteLink;

    @NotNull
    private LocalDateTime megaphoneEndDate;

    @Builder.Default
    private int megaphoneReportedNumber = 0;
}
