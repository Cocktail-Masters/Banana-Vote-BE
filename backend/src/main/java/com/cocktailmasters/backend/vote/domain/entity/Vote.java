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
@AttributeOverride(name = "id", column = @Column(name = "vote_id"))
public class Vote extends BaseEntity {

    @NotNull
    private String voteTitle;

    private String voteContent;
    private String voteImageUrl;
    private String voteThumbnailUrl;

    @Builder.Default
    private Long voteHits = 0L;

    private LocalDateTime voteEndDate;

    @Builder.Default
    private int voteReportedNumber = 0;

    @NotNull
    @Builder.Default
    private boolean isEvent = false;

    @NotNull
    private boolean isAnonymous;

    @NotNull
    private boolean isPublic;

    @NotNull
    @Builder.Default
    private boolean isClosed = false;
}
