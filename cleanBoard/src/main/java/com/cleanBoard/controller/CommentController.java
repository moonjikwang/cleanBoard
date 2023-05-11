package com.cleanBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Comment;
import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.service.CommentSvc;


@Controller
@RequestMapping("comment")
public class CommentController {

	@Autowired
	private CommentSvc commentService;
	
	@PostMapping
	public String register(@RequestParam("num") Long num,
            			   @RequestParam("id") Long id,
            			   @RequestParam("comment") String comment,
            			   RedirectAttributes redirectAttributes) {
		
		Comment reply = Comment.builder().board(Board.builder().num(num).build())
						 .writer(User.builder().id(id).build())
						 .comment(comment)
						 .build();
		
		Comment result = commentService.register(reply);
		redirectAttributes.addAttribute("num",result.getBoard().getNum());//게시글로 다시이동
		System.out.println("리다이렉트 경로 : " + result.getBoard().getCategory());
		return "redirect:/" + result.getBoard().getCategory();
	}
	
	@PutMapping
	public String modify(Comment dto) {
		return null;
	}
	
	@DeleteMapping
	public String remove(Comment dto) {
		return null;
	}
}
