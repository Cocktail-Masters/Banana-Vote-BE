package com.cocktailmasters.backend.goods.domain.entity;

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
@AttributeOverride(name = "id", column = @Column(name = "goods_id"))
@Entity
public class Goods extends BaseEntity {

    @NotNull
    private String goodsName;

    @NotNull
    private String goodsDescription;

    @NotNull
    private Long goodsPrice;

    private LocalDateTime sale_start_date;
    private LocalDateTime sale_end_date;

    @Builder.Default
    private Long goodsSoldNumber = 0L;

    @OneToMany(mappedBy = "goods", cascade = CascadeType.ALL)
    private List<UserGoods> userGoods = new ArrayList<>();
}
