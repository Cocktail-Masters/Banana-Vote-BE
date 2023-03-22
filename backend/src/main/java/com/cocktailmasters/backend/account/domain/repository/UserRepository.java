package com.cocktailmasters.backend.account.domain.repository;

import com.cocktailmasters.backend.account.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
