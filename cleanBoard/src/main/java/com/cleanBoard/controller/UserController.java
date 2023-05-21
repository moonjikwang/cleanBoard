package com.cleanBoard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cleanBoard.model.dtos.UserDTO;
import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.service.UserSvc;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    UserSvc userService;
    
    private final String returnIndex = "redirect:/index";

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 회원가입
     * @param user 회원정보
     * @param redirectAttributes
     * @param req
     * @return
     */
    @PostMapping("signup")
    public String signup(UserDTO user, RedirectAttributes redirectAttributes, HttpServletRequest req) {
        
    	if (userService.validate(user.getUserName())) {
            redirectAttributes.addFlashAttribute("message", "이미 사용중인 아이디입니다.");
            return returnIndex;
        } else {
            HttpSession session = req.getSession();
            User entity = User.builder().userName(user.getUserName()).nickName(user.getNickName())
                    .password(passwordEncoder.encode(user.getPassword())).build();
            User userInfo = userService.signup(entity);
            session.setAttribute("userInfo", userInfo);
            return returnIndex;
        }
    }

    /**
     * 로그인
     * @param user 유저정보
     * @param req
     * @param redirectAttributes
     * @return
     */
    @PostMapping("signin")
    public String signin(UserDTO user, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        
    	HttpSession session = req.getSession();
        User userInfo = userService.signin(user.getUserName(), user.getPassword(), passwordEncoder);
        
        if (userInfo != null) {
            session.setAttribute("userInfo", userInfo);
            return returnIndex;
        } else {
            redirectAttributes.addFlashAttribute("message", "아이디 또는 패스워드를 확인하세요.");
            return returnIndex;
        }
    }

    /**
     * 로그아웃
     * @param req
     * @param redirectAttributes
     * @return
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        
    	req.getSession().invalidate();
        redirectAttributes.addFlashAttribute("message", "로그아웃 되었습니다.");
        
        return returnIndex;
    }

}
