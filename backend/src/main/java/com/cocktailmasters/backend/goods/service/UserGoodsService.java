package com.cocktailmasters.backend.goods.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.domain.entity.User;
import com.cocktailmasters.backend.account.domain.repository.UserRepository;
import com.cocktailmasters.backend.goods.controller.dto.UserGoodsResponse;
import com.cocktailmasters.backend.goods.domain.entity.Goods;
import com.cocktailmasters.backend.goods.domain.entity.UserGoods;
import com.cocktailmasters.backend.goods.domain.repository.GoodsRepository;
import com.cocktailmasters.backend.goods.domain.repository.UserGoodsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserGoodsService {

    private final UserRepository userRepository;
    private final UserGoodsRepository userGoodsRepository;
    private final GoodsRepository goodsRepository;

    /**
     * get list of user goods
     * 
     * @param userId
     * @return list of user goods response
     */
    public List<UserGoodsResponse> getUsersGoods(long userId) {
        List<UserGoods> userGoodsList = userGoodsRepository.findByUserId(userId);
        List<UserGoodsResponse> userGoodsDtos = new ArrayList<>();

        for (UserGoods userGoods : userGoodsList) {
            userGoodsDtos.add(UserGoodsResponse.createUserGoodsReponse(userGoods));
        }

        return userGoodsDtos;
    }

    /**
     * add goods to user
     * 
     * @param goodsId
     * @param userId
     * @return true of false(user or goods not existed)
     */
    @Transactional
    public boolean addUserGoodsToUser(long userId, int quanity, long goodsId) {
        Optional<Goods> addedGoods = goodsRepository.findById(goodsId);
        Optional<User> targetUser = userRepository.findById(userId);
        Optional<UserGoods> optionalUserGoods = userGoodsRepository.findByGoodsIdAndUserId(goodsId, userId);

        if (!addedGoods.isPresent() || !targetUser.isPresent())
            return false;

        UserGoods userGoods;
        if (optionalUserGoods.isPresent()) {
            userGoods = optionalUserGoods.get();
            userGoods.addQuantity(quanity);
        } else {
            userGoods = UserGoods.builder()
                    .goodsAmount(quanity)
                    .isUsing(false)
                    .goodsExpirationDate(LocalDate.of(2100, 12, 31))
                    .user(targetUser.get())
                    .goods(addedGoods.get())
                    .build();
        }

        userGoodsRepository.save(userGoods);
        return true;
    }
}
