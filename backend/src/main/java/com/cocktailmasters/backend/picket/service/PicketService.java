package com.cocktailmasters.backend.picket.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.picket.controller.dto.PicketRequest;
import com.cocktailmasters.backend.picket.controller.dto.item.PicketItem;
import com.cocktailmasters.backend.picket.domain.entity.Picket;
import com.cocktailmasters.backend.picket.domain.repository.PicketRepository;
import com.cocktailmasters.backend.point.service.PointService;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import com.cocktailmasters.backend.vote.domain.repository.VoteRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PicketService {
    
    private final PicketRepository picketRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;

    private final PointService pointService;

    /**
     * get picket list with vote id
     * @param voteId
     * @return List of pickets
     */
    public List<PicketItem> getPicketsInVote(long voteId) {
        List<Picket> pickets = picketRepository.findAllByVoteId(voteId);
        List<PicketItem> picketItems = new ArrayList<>();

        for(Picket picket : pickets)
            picketItems.add(PicketItem.createPicketItem(picket));

        return picketItems;
    }

    /**
     * get last modified time of pickets
     * if there is no picket it return 1999.1.1T0.0.0
     * @param voteId
     * @return localdatetime object (not null)
     */
    public LocalDateTime getLastPicketUpdateTime(long voteId) {
        List<Picket> pickets = picketRepository.findAllByVoteId(voteId);

        // find last modified
        LocalDateTime lastUpdatedTime = LocalDateTime.of(1999, 1, 1, 0, 0);
        for(Picket picket : pickets)
            if(picket.getLastModifiedDate().isAfter(lastUpdatedTime))
                lastUpdatedTime = picket.getLastModifiedDate();

        return lastUpdatedTime;
    }

    /**
     * buy picket with point
     * decrease paid point from user
     * @param voteId
     * @param userId
     * @param picketRequest must value is not null
     * @return -1 if vote is invalid else return current price
     */
    @Transactional
    public long buyPicket(long voteId, long userId, PicketRequest picketRequest) {
        Optional<Vote> vote = voteRepository.findById(voteId);
        if(!vote.isPresent()) return -1;

        int position = picketRequest.getPosition();
        long curPrice = picketRequest.getCurPrice();
        long paidPrice = picketRequest.getPaidPrice();
        String imageUrl = picketRequest.getPicketImageUrl();

        Optional<Picket> picket = picketRepository.findByVoteIdAndPosition(voteId, position);
        // check user Id
        // if(picket.isPresent() && picket.get().getUser().getId() == userId) return -1;

        // apply point
        if(!pointService.addPoint(paidPrice * -1, vote.get().getVoteTitle() + " 투표 " + position + "번째 피켓 구매", userId))
            return -1;

        if(picket.isPresent()) {
            // price conflicted
            if(picket.get().getPrice() != curPrice)
                return picket.get().getPrice();

            picket.get().buyPicket(imageUrl, paidPrice);
            picketRepository.save(picket.get());
        } else {
            picketRepository.save(Picket.builder()
                                    .picketImageUrl(imageUrl)
                                    .price(paidPrice)
                                    .position(position)
                                    .vote(vote.get())
                                    .user(userRepository.findById(userId).get())
                                    .build());
        }

        return paidPrice;
    }

    /**
     * modify picket image
     * @param voteId
     * @param userid
     * @param picketRequest
     * @return true or false
     */
    @Transactional
    public boolean changePicketImage(long voteId, long userId, PicketRequest picketRequest) {
        String modifiedUrl = picketRequest.getPicketImageUrl();
        int position = picketRequest.getPosition();

        Optional<Picket> picket = picketRepository.findByVoteIdAndUserIdAndPosition(voteId, userId, position);
        if(!picket.isPresent()) return false;

        picket.get().changeImage(modifiedUrl);
        picketRepository.save(picket.get());
        return true;
    }

    /**
     * delete picket
     * @param voteId
     * @param position
     * @return true or false
     */
    @Transactional
    public boolean deletePicket(long voteId, int position) {
        Optional<Picket> picket = picketRepository.findByVoteIdAndPosition(voteId, position);
        if(!picket.isPresent()) return false;

        picketRepository.delete(picket.get());
        return true;
    }
}
