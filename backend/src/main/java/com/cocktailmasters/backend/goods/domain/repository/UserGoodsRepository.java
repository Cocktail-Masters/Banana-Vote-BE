package com.cocktailmasters.backend.goods.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.entity.UserGoods;

public interface UserGoodsRepository extends JpaRepository<UserGoods, Long> { }
