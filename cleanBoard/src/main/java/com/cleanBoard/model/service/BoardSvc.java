package com.cleanBoard.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.repository.BoardRep;


@Service
public class BoardSvc {

	@Autowired
	private BoardRep boardRepository;
	
	public Board findById(Long num) {
		Optional<Board> optional = boardRepository.findById(num);
		if(optional.isPresent()) {
			return optional.get();
		}else {
			return null;
		}
	}
	
	public Page<Board> getList(String category,Pageable pageable){
		return boardRepository.findByCategory(category,pageable);
	}
	
	public Board register(Board board) {
		return boardRepository.save(board);
	}
	
	public void remove(Board board) {
		boardRepository.delete(board);
	}
	
}
