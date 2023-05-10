package com.cleanBoard.Domain;

import javax.persistence.*;
import lombok.Builder;

@Builder
@Entity
public class Comment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String comment; // 댓글 내용
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
	@ManyToOne(fetch = FetchType.LAZY)
	private User writer;

    /* 댓글 수정 */
    public void update(String comment) {
        this.comment = comment;
    }
}