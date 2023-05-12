package com.cleanBoard.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Comment;
import com.cleanBoard.model.repository.CommentRep;

@Service
public class CommentSvc {

	@Autowired
	private CommentRep commentRepository;
	
	public Comment register(Comment comment) {
		Comment result = commentRepository.save(comment);
		return result;
	}
	
	public Comment findById(Long id) {
		return commentRepository.findById(id).get();
	}
	
	public void remove(Comment comment) {
		commentRepository.delete(comment);
	}
	
}
