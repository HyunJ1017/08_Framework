package edu.kh.daemoim.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.daemoim.board.dto.Comment;
import edu.kh.daemoim.board.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

	private final CommentMapper mapper;

	// 댓글 등록
	@Override
	public int commentInsert(Comment comment) {
		
		int result = mapper.commentInsert(comment);
		
		if(result > 0) return comment.getCommentNo();
		
		return 0;
	}
	
	// 댓글 수정
	@Override
	public int commentUpdate(Comment comment) {
		return mapper.commentUpdate(comment);
	}
	
	// 댓글 삭제
	@Override
	public int commentDelete(int commentNo, int memberNo) {
		return mapper.commentDelete(commentNo, memberNo);
	}
	
}
