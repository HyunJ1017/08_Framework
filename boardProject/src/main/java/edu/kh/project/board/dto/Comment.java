package edu.kh.project.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Comment {

	private int    commentNo;
	private String commentContent;
	private String commentWriteDate;
	private char   commentDelFl;
	private int    memberNo;
	private int    boardNo;
	private int    parentCommentNo;
	
	/* 댓글에 포함될 작성자명, 작성자 프로필 */
	private String memberNickname;
	private String profileImg;
	
}
