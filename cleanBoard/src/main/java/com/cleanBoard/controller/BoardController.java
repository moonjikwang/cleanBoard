package com.cleanBoard.controller;

import java.awt.print.Pageable;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cleanBoard.Domain.BoardDTO;

@RequestMapping("board")
public class BoardController {

	@GetMapping("list")
	public void list(Model model,Pageable pageable) {
		
	}
	
	@PostMapping
	public String Register(BoardDTO dto) {
		return null;
	}
	@GetMapping
	public String read(Long bno) {
		return null;
	}
	@PutMapping
	public String modify(BoardDTO dto) {
		return null;
	}
	@DeleteMapping
	public String remove(BoardDTO dto) {
		return null;
	}
}
