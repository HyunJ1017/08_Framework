package edu.kh.project.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.board.dto.Comment;
import edu.kh.project.board.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService{

	private final CommentMapper mapper;
	
	// 댓글 번호
	@Override
	public int commentInsert(Comment comment) {
		
		int result = mapper.commentInsert(comment);
		if(result > 0) {
			// 삽입 성공시
			return comment.getCommentNo();
		}
		// 실패시
		return 0;
	}
	
	// 댓글 삭제
	@Override
	public int commentDelete(Comment comment) {
		return mapper.commentDelete(comment);
	}
	
	// 댓글을 수정 하겠다
	@Override
	public int commentUpdate(Comment comment) {
		return mapper.commentUpdate(comment);
	}
}
