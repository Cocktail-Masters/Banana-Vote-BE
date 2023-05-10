package com.cocktailmasters.backend.goods.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.goods.controller.dto.GoodsRequest;
import com.cocktailmasters.backend.goods.controller.dto.GoodsResponse;
import com.cocktailmasters.backend.goods.controller.dto.item.GoodsItemDto;
import com.cocktailmasters.backend.goods.domain.GoodsType;
import com.cocktailmasters.backend.goods.domain.entity.Goods;
import com.cocktailmasters.backend.goods.domain.repository.GoodsRepository;
import com.cocktailmasters.backend.point.service.PointService;
import com.cocktailmasters.backend.util.exception.CustomException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;

    private final PointService pointService;
    private final UserGoodsService userGoodsService;

    /**
     * get all types
     * 
     * @return enum List
     */
    public List<GoodsType> getGoodsTypes() {
        return Arrays.asList(GoodsType.values());
    }

    /**
     * return GoodsType enum by String
     * 
     * @param goodsType
     * @return correct goodstype or null if not exists
     */
    public GoodsType getGoodsTypeByName(String goodsType) {
        for (GoodsType key : GoodsType.values()) {
            if (key.name().equals(goodsType.toUpperCase()))
                return key;
        }
        return null;
    }

    /**
     * find by type or all and return Goods List Response by paging
     * 
     * @param type     if null, find all
     * @param sortBy   2 : sold count, 1 : recently
     * @param page
     * @param pageSize
     * @return GoodsResponse
     */
    public GoodsResponse getGoodsListWithPage(GoodsType type, int sortBy, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        List<Goods> goodsList;
        if (type == null) { // find all
            if (sortBy == 2) // popular first
                goodsList = goodsRepository.findAllByOrderByGoodsSoldNumberAsc(pageable).getContent();
            else // newly first
                goodsList = goodsRepository.findAllByOrderBySaleStartDateAsc(pageable).getContent();
        } else { // find by type
            if (sortBy == 2) // popular first
                goodsList = goodsRepository.findByGoodsTypeOrderByGoodsSoldNumberAsc(type, pageable).getContent();
            else // newly first
                goodsList = goodsRepository.findByGoodsTypeOrderBySaleStartDateAsc(type, pageable).getContent();
        }

        // change to dto
        List<GoodsItemDto> goodsDtosList = new ArrayList<>();
        for (int i = 0; i < goodsList.size(); i++)
            goodsDtosList.add(GoodsItemDto.createGoodItemDto(goodsList.get(i)));

        // get total page
        int totalPages = getGoodsListTotalPages(type, pageSize);

        return new GoodsResponse(totalPages, goodsDtosList);
    }

    /**
     * count total page size by type
     * if type is null. it count by all
     * 
     * @param type
     * @param pageSize
     * @return total page size
     */
    public int getGoodsListTotalPages(GoodsType type, int pageSize) {
        long totalCount;
        if (type == null)
            totalCount = goodsRepository.count();
        else
            totalCount = goodsRepository.countByGoodsType(type);

        return (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * buy goods and apply quantity with point
     * 
     * @param userId
     * @param quantity
     * @param goodsId
     * @return true or false if there is no correct goods id
     * @throws CustomException (lack of quantity, lack of point, out of date)
     */
    @Transactional
    public boolean buyGoods(long userId, int quantity, long goodsId) throws CustomException {
        Optional<Goods> goods = goodsRepository.findById(goodsId);
        if (!goods.isPresent())
            return false;

        // check selling date
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(goods.get().getSaleEndDate()))
            throw new CustomException(HttpStatus.CONFLICT, "out of date");
        // check quantity
        if (goods.get().getGoodsRemainingQuantity() >= 0 && goods.get().getGoodsRemainingQuantity() - quantity < 0)
            throw new CustomException(HttpStatus.CONFLICT, "lack of quantity");

        // apply point
        String logMessage = goods.get().getGoodsName() + " 구매";
        if (!pointService.addPoint(goods.get().getGoodsPrice() * -1 * quantity, logMessage, userId))
            throw new CustomException(HttpStatus.CONFLICT, "lack of point");

        // apply quanity and sold count
        goods.get().soldGoods(quantity);
        goodsRepository.save(goods.get());

        // add goods to user
        if (!userGoodsService.addUserGoodsToUser(userId, quantity, goodsId))
            throw new CustomException(HttpStatus.NOT_FOUND, "user or goods not found");

        return true;
    }

    /**
     * add goods (for admin)
     * 
     * @param goodsRequest
     * @return true
     */
    @Transactional
    public boolean addGoods(GoodsRequest goodsRequest) {
        Goods addedGoods = Goods.builder()
                .goodsName(goodsRequest.getName())
                .goodsDescription(goodsRequest.getDescription())
                .goodsType(goodsRequest.getType())
                .goodsImageUrl(goodsRequest.getImageUrl())
                .goodsValidityPeriod(goodsRequest.getValidPeriod())
                .goodsPrice(goodsRequest.getPrice())
                .goodsRemainingQuantity(goodsRequest.getRemainingQuantity())
                .saleStartDate(goodsRequest.getStartDate())
                .saleEndDate(goodsRequest.getStartDate())
                .build();

        goodsRepository.save(addedGoods);
        return true;
    }

    /**
     * delete goods (for admin)
     * 
     * @param goodsId
     * @param goodsRequest
     * @return true or false(goods not existed)
     */
    @Transactional
    public boolean deleteGoods(long goodsId) {
        Optional<Goods> goods = goodsRepository.findById(goodsId);
        if (!goods.isPresent())
            return false;

        goodsRepository.delete(goods.get());
        return true;
    }

    /**
     * modify goods info (for admin)
     * 
     * @param goodsId
     * @param goodsRequest
     * @return true or false(goods not found)
     */
    @Transactional
    public boolean modifyGoods(long goodsId, GoodsRequest goodsRequest) {
        Optional<Goods> goods = goodsRepository.findById(goodsId);
        if (!goods.isPresent())
            return false;

        goods.get().modifiyGoodsInfo(goodsRequest);
        goodsRepository.save(goods.get());
        return true;
    }
}
