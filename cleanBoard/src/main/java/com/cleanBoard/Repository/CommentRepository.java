package com.cleanBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanBoard.Domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
