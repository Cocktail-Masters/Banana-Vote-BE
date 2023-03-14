package com.cocktailmasters.backend.vote.domain.entity;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "megaphone_id"))
@Entity
public class Megaphone extends BaseEntity {

    @NotNull
    private String megaphoneContent;

    private String voteLink;

    @NotNull
    private LocalDateTime megaphoneEndDate;

    @Builder.Default
    private int megaphoneReportedNumber = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
