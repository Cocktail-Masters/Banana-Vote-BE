package com.cocktailmasters.backend.vote.controller.dto.opinion;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.vote.domain.entity.Agreement;
import com.cocktailmasters.backend.vote.domain.entity.Opinion;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAgreementRequest {

    @NotNull
    private Boolean isAgree;

    public Agreement toAgreementEntity(User user, Opinion opinion) {
        return Agreement.builder()
                .user(user)
                .opinion(opinion)
                .isAgree(isAgree)
                .build();
    }
}
