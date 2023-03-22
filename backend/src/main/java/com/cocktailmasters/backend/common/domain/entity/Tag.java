package com.cocktailmasters.backend.common.domain.entity;

import com.cocktailmasters.backend.account.domain.entity.UserTag;
import com.cocktailmasters.backend.vote.domain.entity.VoteTag;
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
@AttributeOverride(name = "id", column = @Column(name = "tag_id"))
@Entity
public class Tag extends BaseEntity {

    @NotNull
    private String tagName;

    @Builder.Default
    private Long tagUsedNumber = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<VoteTag> voteTags = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<UserTag> userTags = new ArrayList<>();

    public void countTagUsedNumber() {
        this.tagUsedNumber++;
    }
}
