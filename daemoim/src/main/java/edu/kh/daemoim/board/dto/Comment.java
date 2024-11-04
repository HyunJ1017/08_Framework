package edu.kh.daemoim.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Comment {

	/* COMMENT 테이블 컬럼 매핑 필드 */
	private int commentNo;
	private String commentContent;
	private String commentWriteDate;
	private String commentDelFl;
	private int memberNo;
	private int boardNo;
	private int parentCommentNo;
	
	/* 댓글에 포함될 작성자명, 작성자 프로핑 */
	private String memberNickname;
	private String profileImg;
}
