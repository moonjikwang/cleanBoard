package com.cleanBoard.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.repository.UserRep;

@Service
public class UserSvc {

	@Autowired
	UserRep userRepository;
	
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return user.get();
		}else {
			return null;
		}
	}
	
	public User signup(User user) {
		return userRepository.save(user);
	}
	
	public User signin(String userName,String password,PasswordEncoder encoder) {
		User origin = userRepository.findByUserName(userName);
		if(origin != null && encoder.matches(password, origin.getPassword())) {
			return origin;
		}else {
			return null;
		}
	}
	
    public boolean validate(String username) {
        User user = userRepository.findByUserName(username);
        return user != null;
    }
}
