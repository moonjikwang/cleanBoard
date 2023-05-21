package com.cleanBoard.model.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.repository.UserRep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSvc {

    private final UserRep userRepository;

    /**
     * 회원번호로 회원객체리턴
     * @param id 회원번호
     * @return
     */
    public User findById(Long id) {
    	
        Optional<User> user = userRepository.findById(id);
        
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    /**
     * 회원가입
     * @param user 입력받은 회원정보
     * @return
     */
    public User signup(User user) {
        return userRepository.save(user);
    }

    /**
     * 로그인
     * @param userName 입력받은 아이디
     * @param password 입력받은 패스워드
     * @param encoder PasswordEncoder
     * @return
     */
    public User signin(String userName, String password, PasswordEncoder encoder) {
    	
        User origin = userRepository.findByUserName(userName);
        
        if (origin != null && encoder.matches(password, origin.getPassword())) {
            return origin;
        } else {
            return null;
        }
    }

    /**
     * 기존회원여부 검증
     * @param username 아이디
     * @return
     */
    public boolean validate(String username) {
    	
        User user = userRepository.findByUserName(username);
        
        return user != null;
    }
}
