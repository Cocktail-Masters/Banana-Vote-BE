package com.cocktailmasters.backend.account.user.domain.entity;

import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "user_tag_id"))
@Entity
public class UserTag extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
