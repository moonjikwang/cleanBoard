package com.cleanBoard.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanBoard.model.entities.Board;


public interface BoardRep extends JpaRepository<Board, Long> {

    Page<Board> findByCategory(String category, Pageable pageable);
}
