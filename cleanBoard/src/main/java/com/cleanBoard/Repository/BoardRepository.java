package com.cleanBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleanBoard.Domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{

}
