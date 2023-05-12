package com.cleanBoard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Category;
import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.service.BoardSvc;
import com.cleanBoard.model.service.UserSvc;


@Controller
@RequestMapping({"board","notice"})
public class BoardController {

	@Autowired
	private BoardSvc boardService;
	@Autowired
	private UserSvc userService;
	
	@GetMapping
	public String read(Long num,Model model) {
		Board post = boardService.getById(num);
		model.addAttribute("post",post);
		return "board/read";
	}
	
	@GetMapping("list")
	public void list(Model model,Pageable pageable) {
		Page<Board> posts = boardService.getFreeList(Category.FREE.getValue(),PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));
		model.addAttribute("posts", posts);
	}
	
	@GetMapping("write")
	public String write(HttpServletRequest req,RedirectAttributes redirectAttributes) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("userInfo");
		if(user != null) {
			return "/board/write";
		}else {
			redirectAttributes.addFlashAttribute("message","회원만 게시글작성이 가능합니다.");
			return "redirect:/index";
		}
	}
	
	@PostMapping("write")
	public String writePost(@RequestParam("category") String category,
							@RequestParam("id") Long id,
				            @RequestParam("title") String title,
				            @RequestParam("content") String content,
				            RedirectAttributes redirectAttributes) {
		User user = userService.findById(id);
		Board board = Board.builder().category(category).title(title).content(content).writer(user).build();
		Board result = boardService.register(board);
	    return getRedirectURL(result, redirectAttributes);
	}
	
	private String getRedirectURL(Board board, RedirectAttributes redirectAttributes) {
	    redirectAttributes.addAttribute("num", board.getNum());
	    return "redirect:/" + board.getCategory();
	}

}
