package com.cleanBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Category;
import com.cleanBoard.model.service.BoardSvc;


@Controller
@RequestMapping("board")
public class BoardController {

	@Autowired
	private BoardSvc boardService;
	
	@GetMapping("list")
	public void list(Model model,Pageable pageable) {
		Page<Board> posts = boardService.getFreeList(Category.FREE.getValue(),PageRequest.of(pageable.getPageNumber(), 10, Sort.by("regDate").descending()));
		model.addAttribute("posts", posts);
	}
	
	@PostMapping
	public String Register(Board dto) {
		return null;
	}
	
	@GetMapping
	public String read(Long num,Model model) {
		Board post = boardService.getById(num);
		model.addAttribute("post",post);
		return "board/read";
	}
	
	@PutMapping
	public String modify(Board dto) {
		return null;
	}
	
	@DeleteMapping
	public String remove(Board dto) {
		return null;
	}
}
