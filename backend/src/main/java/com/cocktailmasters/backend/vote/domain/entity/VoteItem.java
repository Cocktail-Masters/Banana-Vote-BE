package com.cocktailmasters.backend.vote.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "vote_item_id"))
@Entity
public class VoteItem extends BaseEntity {

    @NotNull
    private int voteItemNumber;

    @NotNull
    private String voteItemTitle;

    private String voteItemImageUrl;
    private String iframeLink;

    @Builder.Default
    private Long totalPoints = 0L;

    @Builder.Default
    private Long bestPoints = 0L;

    @Builder.Default
    private int votedNumber = 0;

    @ManyToOne(fetch = FetchType.EAGER)
    private Vote vote;

    @Builder.Default
    @OneToMany(mappedBy = "voteItem", cascade = CascadeType.ALL)
    private List<Prediction> predictions = new ArrayList<>();

    public void updateVotedNumber() {
        this.votedNumber++;
    }

    public void updateTotalPoints(Long points) {
        this.totalPoints += points;
    }

    public void updateBestPoints(Long points) {
        if (this.bestPoints < points) this.bestPoints = points;
    }

    public void deleteVoteItem() {
        super.delete();
        this.predictions.stream()
                .forEach(Prediction::deletePrediction);
    }
}
