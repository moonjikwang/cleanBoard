package com.cleanBoard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * 댓글 작성폼 전송
     * @param num 게시글 번호
     * @param id 작성자 PK
     * @param comment 댓글 내용
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/register")
    public String registerComment(@RequestParam("num") Long num, @RequestParam("id") Long id,
                                  @RequestParam("comment") String comment, RedirectAttributes redirectAttributes) {

        Board board = boardService.findById(num);
        User writer = userService.findById(id);
        Comment reply = Comment.builder().board(board).writer(writer).comment(comment).build();
        Comment result = commentService.register(reply);

        return getRedirectURL(result.getBoard(), redirectAttributes);
    }

    /**
     * 댓글 삭제
     * @param id 댓글 번호
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/remove") // 댓글 삭제
    public String removeComment(Long id, RedirectAttributes redirectAttributes) {

        Comment comment = commentService.findById(id);

        commentService.remove(comment);

        return getRedirectURL(comment.getBoard(), redirectAttributes);
    }

    
    /** 
     * 게시글로 돌아가기
     * @param board
     * @param redirectAttributes
     * @return String
     */
    private String getRedirectURL(Board board, RedirectAttributes redirectAttributes) {
    	
        redirectAttributes.addAttribute("num", board.getNum());
        
        return "redirect:/" + board.getCategory() + "/read";
    }

}
