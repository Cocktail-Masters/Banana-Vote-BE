package com.cocktailmasters.backend.vote.domain.entity;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.picket.domain.entity.Picket;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "vote_id"))
@Entity
public class Vote extends BaseEntity {

    @NotNull
    private String voteTitle;

    private String voteContent;

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

    @Builder.Default
    private int votedNumber = 0;

    @Builder.Default
    private int opinionNumber = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<Opinion> opinions = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteItem> voteItems = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteTag> voteTags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<Picket> pickets = new ArrayList<>();

    public void updateVotedNumber() {
        this.votedNumber++;
    }

    public void updateVoteHits() {
        this.voteHits++;
    }

    public void addVoteItem(VoteItem voteItem) {
        voteItems.add(voteItem);
    }

    public void addVoteTag(VoteTag voteTag) {
        voteTags.add(voteTag);
    }

    public void deleteVote() {
        super.delete();
        this.voteItems.stream()
                .forEach(VoteItem::deleteVoteItem);
        this.opinions.stream()
                .forEach(Opinion::deleteOpinion);
    }
}
