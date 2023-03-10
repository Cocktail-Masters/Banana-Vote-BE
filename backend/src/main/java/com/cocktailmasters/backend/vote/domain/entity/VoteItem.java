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
@AttributeOverride(name = "id", column = @Column(name = "vote_item_id"))
public class VoteItem extends BaseEntity {

    @NotNull
    private String voteItemTitle;

    private String iframeLink;
    private String voteItemImageUrl;

    @NotNull
    private int voteItemNumber;

    @Builder.Default
    private Long totalPoints = 0L;

    @Builder.Default
    private int votedNumber = 0;
}
