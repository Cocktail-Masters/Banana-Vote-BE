package com.cocktailmasters.backend.goods.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.UserGoods;

public interface UserGoodsRepository extends JpaRepository<UserGoods, Long> {
    List<UserGoods> findByUserId(long userId);

    Optional<UserGoods> findByGoodsIdAndUserId(long goodsId, long userId);
}
