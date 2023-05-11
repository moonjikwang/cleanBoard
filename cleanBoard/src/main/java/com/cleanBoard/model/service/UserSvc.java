package com.cleanBoard.model.service;

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
		return userRepository.findById(id).get();
	}
	
	public User signup(User user) {
		return userRepository.save(user);
	}
	
	public User signin(String userName,String Password,PasswordEncoder encoder) {
		User origin = userRepository.findByUserName(userName);
		if(origin != null && encoder.matches(Password, origin.getPassword())) {
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
