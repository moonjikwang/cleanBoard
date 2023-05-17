package com.cleanBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Category;
import com.cleanBoard.model.service.BoardSvc;


@Controller
public class IndexController {

	@Autowired
	private BoardSvc boardService;
	
    @GetMapping("/")
    public String getIndexPage() {
        return "redirect:index";
    }
    
    @GetMapping("index")
    public void index(Model model) {
    	Pageable pageable = PageRequest.of(0, 5,Sort.by("regDate").descending());
    	Page<Board> posts = boardService.getFreeList(Category.FREE.getValue(),pageable);
    	Page<Board> notice = boardService.getFreeList(Category.NOTICE.getValue(),pageable);
    	model.addAttribute("posts",posts);
    	model.addAttribute("notice",notice);
    }
}