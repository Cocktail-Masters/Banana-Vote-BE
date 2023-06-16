package com.cocktailmasters.backend.goods.domain.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.UserGoods;

public interface UserGoodsRepository extends JpaRepository<UserGoods, Long> {
    List<UserGoods> findByUserId(long userId);

    List<UserGoods> findByUserIdAndIsUsingAndGoodsExpirationDateAfter(long userId, boolean isUsing,
            LocalDate after);

    List<UserGoods> findByUserIdAndIsUsingAndGoodsExpirationDateBefore(long userId, boolean isUsing,
            LocalDate before);

    Optional<UserGoods> findByGoodsIdAndUserId(long goodsId, long userId);
}
