package com.cleanBoard.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("read")
	public void read(Long num,Model model) {
		Board post = boardService.getById(num);
		model.addAttribute("post",post);
	}
	
	@GetMapping("list")
	public void list(Model model,Pageable pageable) {
		Page<Board> posts = boardService.getList(Category.GALLERY.getValue(),PageRequest.of(pageable.getPageNumber(), 12, Sort.by("regDate").descending()));
		model.addAttribute("posts", posts);
	}
	
	@GetMapping("write")
	public String write(HttpServletRequest req,RedirectAttributes redirectAttributes) {
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("userInfo");
		if(user != null) {
			return "gallery/write";
		}else {
			redirectAttributes.addFlashAttribute("message","회원만 작성이 가능합니다.");
			return "redirect:/index";
		}
	}
	
	@GetMapping("modify")
	public String modify(Long num,HttpServletRequest req,Model model,RedirectAttributes redirectAttributes) {
		Board board = boardService.findById(num);
		User user = (User) req.getSession().getAttribute("userInfo");
		
		if(user != null && user.getId() == board.getWriter().getId()) {//요청자와 작성자가 일치여부확인
			model.addAttribute("post",board);
			return "gallery/modify";
		}else {
			redirectAttributes.addFlashAttribute("message","글작성자만 수정할 수 있습니다.");
			return getRedirectURL(board, redirectAttributes);
		}
	}
	
	@GetMapping("remove")
	public String remove(Long num,HttpServletRequest req,RedirectAttributes redirectAttributes) {
		Board board = boardService.findById(num);
		User user = (User) req.getSession().getAttribute("userInfo");
		
		if(user != null && user.getId() == board.getWriter().getId()) {//요청자와 작성자가 일치여부확인
			boardService.remove(board);
			return "redirect:list";
		}else {
			redirectAttributes.addFlashAttribute("message","글작성자만 삭제할 수 있습니다.");
			return "redirect:/cleanBoard/index";
		}
	}
	
	@PostMapping("write")
	public String writePost(@RequestParam("category") String category,
	                        @RequestParam("id") Long id,
	                        @RequestParam("title") String title,
	                        @RequestParam("content") String content,
	                        @RequestParam("image") MultipartFile file,
	                        RedirectAttributes redirectAttributes,HttpServletRequest req) {
            // 이미지 파일 유효성 검사
            String imageUrl = null;
            System.out.println("파일업로드상태"+file);
            if(file != null) {
            	if (!isImageFile(file)) {
                    redirectAttributes.addFlashAttribute("message", "이미지 파일만 업로드할 수 있습니다.");
                    return "redirect:/index";
                }
            	imageUrl = imgUpload(file, req, redirectAttributes);
            }
            // 게시물 등록
            User user = userService.findById(id);
            Board board = Board.builder()
                    .category(category)
                    .imageUrl(imageUrl)
                    .title(title)
                    .content(content)
                    .writer(user)
                    .build();
            Board result = boardService.register(board);
            return getRedirectURL(result, redirectAttributes);

	}

	//이미지 업로드 메소드
	private String imgUpload(MultipartFile file,HttpServletRequest req,RedirectAttributes redirectAttributes) {
        try {
		String path =  req.getSession().getServletContext().getRealPath("/");
    	String folderPath = path+ File.separator+"image"; //폴더 경로
    	File folder = new File(folderPath);
    	
    	if (!folder.exists())
    		folder.mkdir(); //폴더 생성합니다. ("새폴더"만 생성)
    	
        String uploadPath = folder + File.separator;
        // 저장할 파일명 생성
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String filename = UUID.randomUUID().toString() + getFileExtension(originalFilename);
        // 이미지 저장
        file.transferTo(new File(uploadPath + filename));
        // 이미지 URL 설정
        String imageUrl = "/image/" + filename;
        return imageUrl;
        } catch (IOException e) {
            System.out.println("에러내용"+e.getMessage());
            // 예외 처리
            redirectAttributes.addFlashAttribute("message", "사진게시판 게시글 작성에 실패했습니다.");
            return "redirect:/index";
        }
	}
	
	// 이미지 파일 유효성 검사 메소드
	private boolean isImageFile(MultipartFile file) {
	    String contentType = file.getContentType();
	    return contentType != null && contentType.startsWith("image/");
	}

	// 파일 확장자 추출 메소드
	private String getFileExtension(String filename) {
	    return StringUtils.getFilenameExtension(filename);
	}
	
	@PostMapping("modify")
	public String modifyPost(@RequestParam("category") String category,
							 @RequestParam("num") Long num,
							 @RequestParam("id") Long id,
	                         @RequestParam("title") String title,
	                         @RequestParam("content") String content,
	                         @RequestParam("image") MultipartFile file,
				             RedirectAttributes redirectAttributes,HttpServletRequest req) {
		User user = userService.findById(id);
		Board original = boardService.findById(num);
        String imageUrl = original.getImageUrl();
        System.out.println("파일비교:"+file);
        if(imageUrl!=null && !file.isEmpty()) {
        	if (!isImageFile(file)) {
                redirectAttributes.addFlashAttribute("message", "이미지 파일만 업로드할 수 있습니다.");
                return "redirect:/index";
            }
        	imageUrl = imgUpload(file, req, redirectAttributes);
        }
		Board board = Board.builder().category(category).imageUrl(imageUrl).num(num).title(title).content(content).writer(user).build();
		Board result = boardService.register(board);
	    return getRedirectURL(result, redirectAttributes);
	}
	
	private String getRedirectURL(Board board, RedirectAttributes redirectAttributes) {
	    redirectAttributes.addAttribute("num", board.getNum());
	    return "redirect:/" + board.getCategory() + "/read";
	}

}
