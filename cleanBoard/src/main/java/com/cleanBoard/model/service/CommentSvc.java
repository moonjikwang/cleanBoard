package com.cleanBoard.model.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cleanBoard.model.entities.Comment;
import com.cleanBoard.model.repository.CommentRep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentSvc {

    private final CommentRep commentRepository;

    /**
     * 댓글 등록
     * @param comment 댓글정보
     * @return
     */
    public Comment register(Comment comment) {
        return commentRepository.save(comment);
    }

    /**
     * 댓글번호로 댓글 객체리턴
     * @param id 댓글번호
     * @return
     */
    public Comment findById(Long id) {
    	
        Optional<Comment> optional = commentRepository.findById(id);
        
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

    /**
     * 댓글 삭제
     * @param comment
     */
    public void remove(Comment comment) {
        commentRepository.delete(comment);
    }

}
