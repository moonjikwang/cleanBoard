package com.cleanBoard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("board")
public class BoardController {

    @Autowired
    private BoardSvc boardService;
    @Autowired
    private UserSvc userService;

    /**
     * 자유게시판 게시글 상세보기
     * @param num 게시글 번호
     * @param model 
     */
    @GetMapping("read")
    public void read(Long num, Model model) {
    	
        Board post = boardService.findById(num);
        
        model.addAttribute("post", post);
    }

    /**
     * 자유게시판 게시글 목록
     * @param model
     * @param pageable 페이징처리를 위한 Pageable객체
     */
    @GetMapping("list")
    public void list(Model model, @PageableDefault(size = 10, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable) {
    	
        Page<Board> posts = boardService.getList(Category.FREE.getValue(),pageable);
        
        model.addAttribute("posts", posts);
    }

    /**
     * 자유게시판 게시글 작성페이지
     * @param req
     * @param redirectAttributes
     * @return
     */
    @GetMapping("write")
    public String write(HttpServletRequest req, RedirectAttributes redirectAttributes) {
    	
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("userInfo");
        
        if (user != null) {
            return "board/write";
        } else {
            redirectAttributes.addFlashAttribute("message", "회원만 게시글작성이 가능합니다.");
            return "redirect:/index";
        }
    }

    /**
     * 자유게시판 게시글 수정페이지
     * @param num 게시글 번호
     * @param req
     * @param model
     * @param redirectAttributes
     * @return
     */
    @GetMapping("modify")
    public String modify(Long num, HttpServletRequest req, Model model, RedirectAttributes redirectAttributes) {
    	
        Board board = boardService.findById(num);
        User user = (User) req.getSession().getAttribute("userInfo");

        if (user != null && user.getId().equals(board.getWriter().getId())) {// 요청자와 작성자가 일치여부확인
            model.addAttribute("post", board);
            return "board/modify";
        } else {
            redirectAttributes.addFlashAttribute("message", "글작성자만 수정할 수 있습니다.");
            return getRedirectURL(board, redirectAttributes);
        }
    }

    /**
     * 자유게시판 게시글 삭제
     * @param num 게시글 번호
     * @param req
     * @param redirectAttributes
     * @return
     */
    @GetMapping("remove")
    public String remove(Long num, HttpServletRequest req, RedirectAttributes redirectAttributes) {
    	
        Board board = boardService.findById(num);
        User user = (User) req.getSession().getAttribute("userInfo");

        if (user != null && user.getId().equals(board.getWriter().getId())) {// 요청자와 작성자가 일치여부확인
            boardService.remove(board);
            return "redirect:list";
        } else {
            redirectAttributes.addFlashAttribute("message", "글작성자만 삭제할 수 있습니다.");
            return "redirect:/cleanBoard/index";
        }
    }

    /**
     * 자유게시판 게시글작성폼 전송
     * @param category 자유게시판,갤러리,공지사항 구분
     * @param id 작성자 PK
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param redirectAttributes
     * @return
     */
    @PostMapping("write")
    public String writePost(@RequestParam("category") String category, @RequestParam("id") Long id,
                            @RequestParam("title") String title, @RequestParam("content") String content,
                            RedirectAttributes redirectAttributes) {
    	
        User user = userService.findById(id);
        Board board = Board.builder().category(category).title(title).content(content).writer(user).build();
        Board result = boardService.register(board);
        
        return getRedirectURL(result, redirectAttributes);
    }

    /**
     * 자유게시판 게시글 수정폼 전송
     * @param category 게시글 구분
     * @param num 게시글 번호
     * @param id 작성자 PK
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param redirectAttributes
     * @return
     */
    @PostMapping("modify")
    public String modifyPost(@RequestParam("category") String category, @RequestParam("num") Long num,
                             @RequestParam("id") Long id, @RequestParam("title") String title, @RequestParam("content") String content,
                             RedirectAttributes redirectAttributes) {
    	
        User user = userService.findById(id);
        Board board = Board.builder().category(category).num(num).title(title).content(content).writer(user).build();
        Board result = boardService.register(board);
        
        return getRedirectURL(result, redirectAttributes);
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
