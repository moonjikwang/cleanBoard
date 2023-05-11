package com.cleanBoard.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanBoard.model.entities.User;

public interface UserRep extends JpaRepository<User, Long>{

	User findByUserName(String userName);
}
