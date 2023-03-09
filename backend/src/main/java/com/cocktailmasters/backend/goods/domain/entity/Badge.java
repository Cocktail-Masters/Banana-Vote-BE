package com.cocktailmasters.backend.goods.domain.entity;

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
@AttributeOverride(name = "id", column = @Column(name = "badge_id"))
public class Badge {

    @NotNull
    private String badgeName;

    @NotNull
    private String badgeImageUrl;

    @NotNull
    private String badgeDescription;


    private String badgePrice;
    private boolean isSelling;
    private LocalDateTime badgeEndDate;

    @Builder.Default
    private Long badgeSoldNumber = 0L;
}
