package com.cocktailmasters.backend.vote.domain.entity;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "prediction_id"))
@Entity
public class Prediction extends BaseEntity {

    @Builder.Default
    private Long predictionPoints = 0L;

    @Builder.Default
    private boolean isReceivePoints = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    private VoteItem voteItem;

    public void updatePredictionPoints(Long predictionPoints) {
        this.predictionPoints += predictionPoints;
    }

    public void setPredictionEnd() {
        this.isReceivePoints = true;
    }

    public void deletePrediction() {
        super.delete();
    }
}
