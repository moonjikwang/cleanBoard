package com.cleanBoard.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Category;
import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.service.BoardSvc;
import com.cleanBoard.model.service.UserSvc;

@Controller
@RequestMapping("gallery")
public class GalleryController {

    @Autowired
    private BoardSvc boardService;
    @Autowired
    private UserSvc userService;
    private final String returnIndex = "redirect:/index";

    /**
     * 사진게시판 게시글 상세보기
     * @param num 게시글 번호
     * @param model
     */
    @GetMapping("read")
    public void read(Long num, Model model) {
    	
        Board post = boardService.findById(num);
        
        model.addAttribute("post", post);
    }

    /**
     * 사진게시판 리스트 출력
     * @param model
     * @param pageable
     */
    @GetMapping("list")
    public void list(Model model, @PageableDefault(size = 12, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable) {
    	
        Page<Board> posts = boardService.getList(Category.GALLERY.getValue(),pageable);
        
        model.addAttribute("posts", posts);
    }

    /**
     * 사진게시판 게시글 작성페이지
     * @param req
     * @param redirectAttributes
     * @return
     */
    @GetMapping("write")
    public String write(HttpServletRequest req, RedirectAttributes redirectAttributes) {
    	
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("userInfo");
        
        if (user != null) {
            return "gallery/write";
        } else {
            redirectAttributes.addFlashAttribute("message", "회원만 작성이 가능합니다.");
            return returnIndex;
        }
    }

    /**
     * 사진게시판 게시글 수정 페이지
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
            return "gallery/modify";
        } else {
            redirectAttributes.addFlashAttribute("message", "글작성자만 수정할 수 있습니다.");
            return getRedirectURL(board, redirectAttributes);
        }
    }

    /**
     * 사진게시판 게시글 삭제
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
     * 사진게시판 게시글작성폼 전송
     * @param category 게시글 구분
     * @param id 작성자 PK
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param file 게시글 첨부파일
     * @param redirectAttributes
     * @param req
     * @return
     */
    @PostMapping("write")
    public String writePost(@RequestParam("category") String category, @RequestParam("id") Long id,
                            @RequestParam("title") String title, @RequestParam("content") String content,
                            @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest req) {
        
        String imageUrl = null;
        User user = userService.findById(id);
        
        if (file != null) {
            if (!isImageFile(file)) {
                redirectAttributes.addFlashAttribute("message", "이미지 파일만 업로드할 수 있습니다.");
                return returnIndex;
            }
            imageUrl = imgUpload(file, req, redirectAttributes);
        }
        // 게시물 등록
        Board board = Board.builder().category(category).imageUrl(imageUrl).title(title).content(content).writer(user).build();
        Board result = boardService.register(board);
        
        return getRedirectURL(result, redirectAttributes);

    }

    /**
     * 이미지 업로드 메소드
     * @param file 전송받은 파일
     * @param req
     * @param redirectAttributes
     * @return
     */
    private String imgUpload(MultipartFile file, HttpServletRequest req, RedirectAttributes redirectAttributes) {
        
    	try {
            String path = req.getSession().getServletContext().getRealPath("/");
            String folderPath = path + File.separator + "image"; // 폴더 경로
            File folder = new File(folderPath);

            if (!folder.exists())
                folder.mkdir(); // 폴더 생성합니다. ("새폴더"만 생성)

            String uploadPath = folder + File.separator;
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String filename = UUID.randomUUID().toString() + getFileExtension(originalFilename);
            
            file.transferTo(new File(uploadPath + filename));
            
            String imageUrl = "/image/" + filename;
            
            return imageUrl;
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "사진게시판 게시글 작성에 실패했습니다.");
            return returnIndex;
        }
    }

    /**
     * 이미지 파일 유효성 검사 메소드
     * @param file
     * @return
     */
    private boolean isImageFile(MultipartFile file) {
    	
        String contentType = file.getContentType();
        
        return contentType != null && contentType.startsWith("image/");
    }

    /**
     * 파일 확장자 추출 메소드
     * @param filename
     * @return
     */
    private String getFileExtension(String filename) {
        return StringUtils.getFilenameExtension(filename);
    }

    /**
     * 사진게시판 게시글 수정폼 전송
     * @param category 게시글 구분
     * @param num 게시글 번호
     * @param id 작성자 PK
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param file 게시글 첨부파일
     * @param redirectAttributes
     * @param req
     * @return
     */
    @PostMapping("modify")
    public String modifyPost(@RequestParam("category") String category, @RequestParam("num") Long num,
                             @RequestParam("id") Long id, @RequestParam("title") String title, @RequestParam("content") String content,
                             @RequestParam("image") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest req) {
        
    	User user = userService.findById(id);
        Board original = boardService.findById(num);
        String imageUrl = original.getImageUrl();
        
        if (imageUrl != null && !file.isEmpty()) {
            if (!isImageFile(file)) {
                redirectAttributes.addFlashAttribute("message", "이미지 파일만 업로드할 수 있습니다.");
                return returnIndex;
            }
            imageUrl = imgUpload(file, req, redirectAttributes);
        }
        
        Board board = Board.builder().category(category).imageUrl(imageUrl).num(num).title(title).content(content).writer(user).build();
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
