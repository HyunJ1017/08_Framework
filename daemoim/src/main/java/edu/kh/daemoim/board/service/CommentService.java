package edu.kh.daemoim.board.service;

import edu.kh.daemoim.board.dto.Comment;

public interface CommentService {

	/** 댓글 등록
	 * @param comment
	 * @return
	 */
	int commentInsert(Comment comment);

	/** 댓글 수정
	 * @param comment
	 * @return
	 */
	int commentUpdate(Comment comment);

	/** 댓글 삭제
	 * @param commentNo
	 * @param memberNo
	 * @return
	 */
	int commentDelete(int commentNo, int memberNo);

}
