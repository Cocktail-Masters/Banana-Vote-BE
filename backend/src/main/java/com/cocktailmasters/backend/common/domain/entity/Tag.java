package com.cocktailmasters.backend.common.domain.entity;

import com.cocktailmasters.backend.vote.domain.entity.VoteTag;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "tag")
    private List<VoteTag> voteTags = new ArrayList<>();
}
