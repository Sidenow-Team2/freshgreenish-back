package com.sidenow.freshgreenish.domain.user.repository;

import com.sidenow.freshgreenish.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long> {
}