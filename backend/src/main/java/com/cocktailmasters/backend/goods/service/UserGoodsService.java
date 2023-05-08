package com.cocktailmasters.backend.goods.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cocktailmasters.backend.goods.controller.dto.UserGoodsResponse;
import com.cocktailmasters.backend.goods.domain.entity.UserGoods;
import com.cocktailmasters.backend.goods.domain.repository.UserGoodsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserGoodsService {
    
    private UserGoodsRepository userGoodsRepository;

    /**
     * get list of user goods
     * @param userId
     * @return list of user goods response
     */
    public List<UserGoodsResponse> getUsersGoods(long userId) {
        List<UserGoods> userGoodsList = userGoodsRepository.findByUserId(userId);
        List<UserGoodsResponse> userGoodsDtos = new ArrayList<>();

        for(UserGoods userGoods : userGoodsList) {
            userGoodsDtos.add(UserGoodsResponse.createUserGoodsReponse(userGoods));
        }

        return userGoodsDtos;
    }
}
