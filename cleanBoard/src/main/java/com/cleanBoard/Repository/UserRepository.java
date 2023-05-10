package com.cleanBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanBoard.Domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
