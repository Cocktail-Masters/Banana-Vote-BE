package com.cocktailmasters.backend.account.domain.entity;

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
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseEntity {

    @NotNull
    private String nickname;

    private String gender;

    @Builder.Default
    private Boolean isAdmin = false;

    @Builder.Default
    private Long points = 0L;

    private LocalDateTime banReleaseDate;

    @Deprecated
    private int ban_number = 0;
}
