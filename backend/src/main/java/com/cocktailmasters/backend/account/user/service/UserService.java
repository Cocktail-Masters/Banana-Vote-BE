package com.cocktailmasters.backend.account.user.service;

import com.cocktailmasters.backend.account.user.controller.dto.*;
import com.cocktailmasters.backend.account.user.controller.dto.item.MyVoteDto;
import com.cocktailmasters.backend.account.user.controller.dto.item.VoteDto;
import com.cocktailmasters.backend.account.user.domain.entity.Gender;
import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.entity.UserTag;
import com.cocktailmasters.backend.account.user.domain.repository.UserRepository;
import com.cocktailmasters.backend.account.user.domain.repository.UserTagRepository;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import com.cocktailmasters.backend.common.domain.repository.TagRepository;
import com.cocktailmasters.backend.season.service.RankingService;
import com.cocktailmasters.backend.util.exception.NotFoundUserException;
import com.cocktailmasters.backend.vote.domain.entity.Vote;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final RankingService rankingService;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final UserTagRepository userTagRepository;

    @Transactional
    public boolean signUp(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            return false;
        }
        if (userRepository.findByNickname(signUpRequest.getNickname()).isPresent()) {
            return false;
        }
        User user = signUpRequest.toUserEntity();
        user.encodePassword(passwordEncoder);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean signIn(SignInRequest signInRequest) {
        User user = userRepository.findByEmailAndIsActiveTrue(signInRequest.getEmail())
                .orElse(null);
        if (user == null) {
            return false;
        }
        if (passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())) {
            return true;
        }
        return false;
    }

    public boolean signOut(User user) {
        user.updateRefreshToken("");
        return true;
    }

    @Transactional
    public boolean withdrawal(User user) {
        user.deleteUser();
        userRepository.save(user);
        return true;
    }

    public FindUserInfoResponse findUserInfo(User user, long findUserId) {
        if (user != null && user.getId() == findUserId) {
            return FindUserInfoResponse.builder()
                    .nickname(user.getNickname())
                    .equippedBadgeImageUrl(user.getEquippedBadgeImageUrl())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .ranking(rankingService.getCurrentSeasonUserRanking(user.getId()))
                    .points(user.getPoints())
                    .build();
        }
        User findUser = userRepository.findById(findUserId)
                .orElse(null);
        if (findUser == null) {
            throw new NotFoundUserException();
        }
        return FindUserInfoResponse.builder()
                .nickname(findUser.getNickname())
                .equippedBadgeImageUrl(findUser.getEquippedBadgeImageUrl())
                .ranking(rankingService.getCurrentSeasonUserRanking(findUser.getId()))
                .build();
    }

    @Transactional
    public boolean updateNickname(User user, UpdateNicknameRequest updateNicknameRequest) {
        String newNickname = updateNicknameRequest.getNickname();
        if (!userRepository.findByNickname(newNickname).isEmpty()) {
            return false;
        }
        user.updateNickname(updateNicknameRequest.getNickname());
        user.updateRoleGuestToUser();
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean updateAge(User user, UpdateAgeRequest updateAgeRequest) {
        int age = updateAgeRequest.getAge();
        if (age <= 0 || age >= 150) {
            return false;
        }
        user.updateAge(age);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public boolean updateGender(User user, UpdateGenderRequest updateGenderRequest) {
        Gender gender = updateGenderRequest.getGender();
        user.updateGender(gender);
        userRepository.save(user);
        return true;
    }

    public FindInterestTagsResponse findInterestTags(User user) {
        List<UserTag> userTags = userTagRepository.findAllByUser(user);
        return FindInterestTagsResponse.builder()
                .tags(userTags.stream()
                        .map(userTag -> userTag.getTag().getTagName())
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void createInterestTag(User user, String tagName) {
        Tag tag = tagRepository.findByTagName(tagName)
                .orElse(null);
        if (tag == null) {
            tag = Tag.builder()
                    .tagName(tagName)
                    .build();
            tagRepository.saveAndFlush(tag);
        }
        UserTag userTag = createUserTag(user, tag);
        user.addUserTag(userTag);
        userRepository.save(user);
    }

    @Transactional
    public boolean deleteInterestTag(User user, String TagName) {
        Tag tag = tagRepository.findByTagName(TagName)
                .orElse(null);
        if (tag == null) {
            return false;
        }
        UserTag userTag = userTagRepository.findByTag(tag)
                .orElse(null);
        if (userTag == null) {
            return false;
        }
        userTagRepository.delete(userTag);
        return true;
    }

    public FindParticipateVotesResponse findParticipateVotes(User user) {
        return FindParticipateVotesResponse.builder()
                .votes(user.getPredictions()
                        .stream()
                        .map(prediction -> {
                            Vote vote = prediction.getVoteItem().getVote();
                            return VoteDto.builder()
                                    .id(vote.getId())
                                    .title(vote.getVoteTitle())
                                    .isClosed(vote.isClosed())
                                    .predictedPoint(prediction.getPredictionPoints())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
    }

    public FindMyVotesResponse findMyVotes(User user) {
        return FindMyVotesResponse.builder()
                .votes(user.getVotes()
                        .stream()
                        .map(vote -> MyVoteDto.builder()
                                .id(vote.getId())
                                .title(vote.getVoteTitle())
                                .isClosed(vote.isClosed()).build())
                        .collect(Collectors.toList()))
                .build();
    }

    private UserTag createUserTag(User user, Tag tag) {
        UserTag userTag = UserTag.builder()
                .tag(tag)
                .user(user)
                .build();
        return userTag;
    }
}
