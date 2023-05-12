package com.cleanBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Comment;
import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.service.BoardSvc;
import com.cleanBoard.model.service.CommentSvc;
import com.cleanBoard.model.service.UserSvc;


@Controller
@RequestMapping("comment")
public class CommentController {

	@Autowired
	private CommentSvc commentService;
	@Autowired
	private BoardSvc boardService;
	@Autowired
	private UserSvc userService;
	
	@PostMapping("/register") // 댓글 등록
	public String registerComment(@RequestParam("num") Long num,
	                              @RequestParam("id") Long id,
	                              @RequestParam("comment") String comment,
	                              RedirectAttributes redirectAttributes) {

	    Board board = boardService.findById(num);
	    User writer = userService.findById(id);

	    Comment reply = Comment.builder()
	            .board(board)
	            .writer(writer)
	            .comment(comment)
	            .build();

	    Comment result = commentService.register(reply);

	    return getRedirectURL(result.getBoard(), redirectAttributes);
	}

	@GetMapping("/remove") // 댓글 삭제
	public String removeComment(Long id, RedirectAttributes redirectAttributes) {

	    Comment comment = commentService.findById(id);
	    commentService.remove(comment);

	    return getRedirectURL(comment.getBoard(), redirectAttributes);
	}

	private String getRedirectURL(Board board, RedirectAttributes redirectAttributes) {
	    redirectAttributes.addAttribute("num", board.getNum());
	    return "redirect:/" + board.getCategory();
	}
	
}
