package edu.kh.project.board.service;

import edu.kh.project.board.dto.Comment;

public interface CommentService {

	/** 댓글 등록
	 * @param comment
	 * @return commentNo
	 */
	int commentInsert(Comment comment);

	/** 댓글을 삭제하시겠다
	 * @param comment
	 * @return 결과값 반환하겠다
	 */
	int commentDelete(Comment comment);

	/** 댓글을 수정하겠다
	 * @param comment
	 * @return 0 || 1
	 */
	int commentUpdate(Comment comment);

}
