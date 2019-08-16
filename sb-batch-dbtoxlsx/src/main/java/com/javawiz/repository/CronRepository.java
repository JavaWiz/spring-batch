package com.javawiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javawiz.entity.CronEntity;

public interface CronRepository extends JpaRepository<CronEntity, Long> {
}
