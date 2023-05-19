package com.cleanBoard.model.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cleanBoard.model.entities.Comment;
import com.cleanBoard.model.repository.CommentRep;

@Service
public class CommentSvc {

	@Autowired
	private CommentRep commentRepository;
	
	public Comment register(Comment comment) {
		return commentRepository.save(comment);
	}
	
	public Comment findById(Long id) {
		Optional<Comment> optional = commentRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else {
			return null;
		}
	}
	
	public void remove(Comment comment) {
		commentRepository.delete(comment);
	}
	
}
