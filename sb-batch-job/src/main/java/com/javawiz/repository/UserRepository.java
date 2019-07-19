package com.javawiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javawiz.dto.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
