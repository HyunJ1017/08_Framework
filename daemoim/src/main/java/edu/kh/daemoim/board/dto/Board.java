package edu.kh.daemoim.board.dto;

import java.util.List;

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
public class Board {

	private int rnum;
	
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	private String boardWriteDate;
	private String boardUpdateDate;
	private int 	 readCount;
	private String boardDelFl;
	private int 	 memberNo;
	private int 	 boardTypeCode;
	private int 	 groupNo;
	
	//MEMBER 테이블 JOIN 컬럼
	private String memberNickname;
	
	// 목록 조회 시 댓글/좋아요 수 상관 쿼리 결과
	private int		 commentCount;
	private int		 likeCount;
	
	private String thumbnail; // 썸네일 이미지
	private String memberImg; // 작성자 프로필 이미지
	
	//특정 게시글의 이미지 목록을 저장할 필드
	private List<BoardImg> imageList;
	
	// 특정 게시글의 댓글 목록을 저장할 필드
	private List<Comment> commentList;
	
	// 좋아요 체크 여부를 저장할 필드(1 == 누른적 있음)
	private int likeCheck;

}
	
