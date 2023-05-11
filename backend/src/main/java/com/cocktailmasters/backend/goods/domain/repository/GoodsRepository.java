package com.cocktailmasters.backend.goods.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Page<Goods> findByGoodsTypeOrderBySaleStartDateAsc(GoodsType goodstype, Pageable pageable);

    Page<Goods> findByGoodsTypeOrderByGoodsSoldNumberAsc(GoodsType goodstype, Pageable pageable);

    Page<Goods> findByGoodsTypeOrderByGoodsPriceAsc(GoodsType goodstype, Pageable pageable);

    Page<Goods> findByGoodsTypeOrderByGoodsPriceDec(GoodsType goodstype, Pageable pageable);

    Page<Goods> findAllByOrderBySaleStartDateAsc(Pageable pageable);

    Page<Goods> findAllByOrderByGoodsSoldNumberAsc(Pageable pageable);

    Page<Goods> findAllByOrderByGoodsPriceAsc(Pageable pageable);

    Page<Goods> findAllByOrderByGoodsPriceDec(Pageable pageable);

    long countByGoodsType(GoodsType goodsType);
}