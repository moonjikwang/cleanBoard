package com.cleanBoard.model.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.repository.BoardRep;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class BoardSvc {

    private final BoardRep boardRepository;

    /**
     * 게시글번호로 게시글 객체리턴
     * @param num
     * @return
     */
    public Board findById(Long num) {
    	
        Optional<Board> optional = boardRepository.findById(num);
        
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    /**
     * category,pageable 로 게시글 Page객체리턴
     * @param category 게시글 구분 (notice,board,gallery)
     * @param pageable 
     * @return
     */
    public Page<Board> getList(String category, Pageable pageable) {
        return boardRepository.findByCategory(category, pageable);
    }

    
    /** 
     * 게시글 등록
     * @param board
     * @return Board
     */
    public Board register(Board board) {
        return boardRepository.save(board);
    }

    public void remove(Board board) {
        boardRepository.delete(board);
    }

}
