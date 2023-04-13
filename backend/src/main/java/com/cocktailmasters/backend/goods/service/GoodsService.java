package com.cocktailmasters.backend.goods.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.goods.controller.dto.GoodsResponse;
import com.cocktailmasters.backend.goods.controller.dto.item.GoodsItemDto;
import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.Goods;
import com.cocktailmasters.backend.goods.domain.repository.GoodsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GoodsService {
    
    private final GoodsRepository goodsRepository;

    /**
     * get all types
     * @return enum List
     */
    public List<GoodsType> getGoodsTpyes() {
        return Arrays.asList(GoodsType.values());
    }

    /**
     * return GoodsType enum by String
     * @param goodsType
     * @return correct goodstype or null if not exists
     */
    public GoodsType getGoodsTypeByName(String goodsType) {
        for(GoodsType key : GoodsType.values()) {
            if(key.name().equals(goodsType.toUpperCase()))
                return key;
        }
        return null;
    }

    /**
     * find by type or all and return Goods List Response by paging
     * @param type if null, find all
     * @param sortBy 2 : sold count, 1 : recently
     * @param page
     * @param pageSize
     * @return GoodsResponse
     */
    public GoodsResponse getGoodsListWithPage(GoodsType type, int sortBy, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        List<Goods> goodsList;
        if(type == null) { // find all
            if(sortBy == 2) // popular first
                goodsList = goodsRepository.findAllByOrderByGoodsSoldNumberAsc(pageable).getContent();
            else // newly first
                goodsList = goodsRepository.findAllByOrderBySaleStartDateAsc(pageable).getContent();
        } else { // find by type
            if(sortBy == 2) // popular first
                goodsList = goodsRepository.findByGoodsTypeOrderByGoodsSoldNumberAsc(type, pageable).getContent();
            else // newly first
                goodsList = goodsRepository.findByGoodsTypeOrderBySaleStartDateAsc(type, pageable).getContent();
        }

        // change to dto
        List<GoodsItemDto> goodsDtosList = new ArrayList<>();
        for(int i = 0; i < goodsList.size(); i++)
            goodsDtosList.add(GoodsItemDto.createGoodItemDto(goodsList.get(i)));

        // get total page
        int totalPages = getGoodsListTotalPages(type, pageSize);

        return new GoodsResponse(totalPages, goodsDtosList);
    }

    /**
     * count total page size by type
     * if type is null. it count by all
     * @param type
     * @param pageSize
     * @return total page size
     */
    public int getGoodsListTotalPages(GoodsType type, int pageSize) {
        long totalCount;
        if(type == null)
            totalCount = goodsRepository.count();
        else
            totalCount = goodsRepository.countByGoodsType(type);

        return (int) Math.ceil((double) totalCount / pageSize);
    }
}
