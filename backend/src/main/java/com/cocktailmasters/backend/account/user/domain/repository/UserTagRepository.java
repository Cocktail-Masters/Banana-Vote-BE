package com.cocktailmasters.backend.account.user.domain.repository;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import com.cocktailmasters.backend.account.user.domain.entity.UserTag;
import com.cocktailmasters.backend.common.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {

    Optional<UserTag> findByTag(Tag tag);

    List<UserTag> findAllByUser(User user);
}
