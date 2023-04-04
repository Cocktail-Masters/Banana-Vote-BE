package com.cocktailmasters.backend.account.domain.repository;

import com.cocktailmasters.backend.account.domain.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);

    @Modifying
    @Query("UPDATE User u SET u.points = :point WHERE u.id = :id")
    int updateUserPointById(@Param("id")Long id, @Param("point")Long point);
}
