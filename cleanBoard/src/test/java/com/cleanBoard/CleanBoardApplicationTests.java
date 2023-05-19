package com.cleanBoard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cleanBoard.model.entities.Board;
import com.cleanBoard.model.entities.Category;
import com.cleanBoard.model.entities.User;
import com.cleanBoard.model.service.BoardSvc;
import com.cleanBoard.model.service.UserSvc;


@SpringBootTest
class CleanBoardApplicationTests {

	@Autowired
	BoardSvc boardService;
	@Autowired
	UserSvc userService;
	
	@Test
	void contextLoads() {
		System.out.println("test");
	}

}
