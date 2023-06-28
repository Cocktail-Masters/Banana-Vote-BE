package com.cocktailmasters.backend.goods.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.Goods;

public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Page<Goods> findByGoodsTypeOrderBySaleStartDateDesc(GoodsType goodstype, Pageable pageable);

    Page<Goods> findByGoodsTypeOrderByGoodsSoldNumberDesc(GoodsType goodstype, Pageable pageable);

    Page<Goods> findByGoodsTypeOrderByGoodsPriceAsc(GoodsType goodstype, Pageable pageable);

    Page<Goods> findByGoodsTypeOrderByGoodsPriceDesc(GoodsType goodstype, Pageable pageable);

    Page<Goods> findAllByOrderBySaleStartDateDesc(Pageable pageable);

    Page<Goods> findAllByOrderByGoodsSoldNumberDesc(Pageable pageable);

    Page<Goods> findAllByOrderByGoodsPriceAsc(Pageable pageable);

    Page<Goods> findAllByOrderByGoodsPriceDesc(Pageable pageable);

    long countByGoodsType(GoodsType goodsType);
}
