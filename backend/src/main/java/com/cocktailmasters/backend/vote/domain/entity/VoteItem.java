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
    private int votedNumber = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Vote vote;

    @Builder.Default
    @OneToMany(mappedBy = "voteItem", cascade = CascadeType.ALL)
    private List<Prediction> predictions = new ArrayList<>();
}
