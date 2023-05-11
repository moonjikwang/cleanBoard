package com.cleanBoard.model.service;

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
	
	public Page<Board> getFreeList(String category,Pageable pageable){
		return boardRepository.findByCategory(category,pageable);
	}
	
	public Long register(Board board) {
		Board result = boardRepository.save(board);
		return result.getNum();
	}
	
	public Board getById(Long num) {
		return boardRepository.findById(num).get();
	}
	
}
