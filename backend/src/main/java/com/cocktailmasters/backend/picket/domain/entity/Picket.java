package com.cocktailmasters.backend.picket.domain.entity;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.common.domain.entity.BaseEntity;
import com.cocktailmasters.backend.vote.domain.entity.Vote;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "picket_id"))
@Entity
public class Picket extends BaseEntity {

    @NotNull
    private String picketImageUrl;

    private int position;

    private Long price;

    @Builder.Default
    private int picketReportedNumber = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Vote vote;

    public void buyPicket(String url, long price) {
        this.picketImageUrl = url;
        this.price = price;
    }

    public void changeImage(String url) {
        this.picketImageUrl = url;
    }
}
