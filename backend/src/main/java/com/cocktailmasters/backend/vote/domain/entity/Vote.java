package com.cocktailmasters.backend.vote.domain.entity;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
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
    private String voteImageUrl;
    private String voteThumbnailUrl;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Opinion opinion;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteItem> voteItems = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<VoteTag> voteTags = new ArrayList<>();

    @OneToMany(mappedBy = "vote", cascade = CascadeType.ALL)
    private List<Picket> pickets = new ArrayList<>();
}
