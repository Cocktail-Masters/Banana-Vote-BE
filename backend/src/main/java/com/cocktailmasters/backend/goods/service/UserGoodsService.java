package com.cocktailmasters.backend.goods.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.user.controller.dto.MegaphoneRequest;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.goods.controller.dto.UserGoodsResponse;
import com.cocktailmasters.backend.goods.domain.entity.Goods;
import com.cocktailmasters.backend.goods.domain.entity.UserGoods;
import com.cocktailmasters.backend.goods.domain.repository.GoodsRepository;
import com.cocktailmasters.backend.goods.domain.repository.UserGoodsRepository;
import com.cocktailmasters.backend.megaphone.service.MegaphoneService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserGoodsService {

    private final UserRepository userRepository;
    private final UserGoodsRepository userGoodsRepository;
    private final GoodsRepository goodsRepository;

    private final MegaphoneService megaphoneService;

    /**
     * get list of user goods
     * 
     * @param userId
     * @return list of user goods response
     */
    @Transactional
    public List<UserGoodsResponse> getUsersGoods(long userId, boolean isUsing) {
        List<UserGoods> userGoodsList;
        if (isUsing)
            userGoodsList = userGoodsRepository.findByUserIdAndIsUsingAndGoodsExpirationDateBefore(userId, isUsing,
                    LocalDate.now());
        else
            userGoodsList = userGoodsRepository.findByUserId(userId);

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
                    .user(targetUser.get())
                    .goods(addedGoods.get())
                    .build();
        }

        userGoodsRepository.save(userGoods);
        return true;
    }

    /**
     * use Goods
     * 
     * @param goodsId          to use
     * @param userId
     * @param megaphoneRequest if it using magaphone
     * @return 1(success) or 0(not found) or -1(invalid request)
     */
    @Transactional
    public int useGoods(long userGoodsId, long userId, MegaphoneRequest megaphoneRequest) {
        Optional<UserGoods> userGoods = userGoodsRepository.findById(userGoodsId);

        // if goods not found
        if (!userGoods.isPresent())
            return 0;

        // invalid amount
        if (userGoods.get().getGoodsAmount() == 0)
            return -1;

        Goods goodsInfo = userGoods.get().getGoods();
        long addedDate = goodsInfo.getGoodsUsingPeriod();

        switch (goodsInfo.getGoodsType()) {
            case COSMETIC:
                if (userGoods.get().isUsing())
                    userGoods.get().extendDate(addedDate);
                else
                    userGoods.get().startUsing(addedDate);
                break;
            case MEGAPHONE:
                // apply megaphone
                if (!megaphoneService.addMegaphone(megaphoneRequest, addedDate, userId))
                    return -1;
                break;
            default:
                // unknown type
                return -1;
        }

        userGoodsRepository.save(userGoods.get());
        return 1;
    }
}
