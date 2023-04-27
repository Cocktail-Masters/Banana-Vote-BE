package com.cocktailmasters.backend.account.user.domain.repository;

import com.cocktailmasters.backend.account.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    @Modifying
    @Query("UPDATE User u SET u.points = :point WHERE u.id = :id")
    int updateUserPointById(@Param("id") Long id, @Param("point") Long point);
}
