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
@AttributeOverride(name = "id", column = @Column(name = "picket_id"))
public class Picket extends BaseEntity {

    @NotNull
    private String picketImageUrl;

    private int position;
    private Long price;

    @Builder.Default
    private int picketReportedNumber = 0;
}
