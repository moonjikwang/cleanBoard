package com.cleanBoard.Domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BoardDTO {

	private Long bno;
    private String title;
    private String content;
	private String category;
    private int view;
	private User writer;
	private List<Comment> comments;
	
}
