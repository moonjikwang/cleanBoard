package com.cleanBoard.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanBoard.model.entities.Comment;

public interface CommentRep extends JpaRepository<Comment, Long>{

}
