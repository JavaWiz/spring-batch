package com.javawiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javawiz.entity.People;

public interface PeopleRepository extends JpaRepository<People, Integer>{
}
