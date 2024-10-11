package edu.kh.project.board.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.dto.Comment;

@Mapper
public interface CommentMapper {

	// 댓글 등록 하기
	int commentInsert(Comment comment);

	// 댓글 삭제하기
	int commentDelete(Comment comment);

	// 댓글 수정하기
	int commentUpdate(Comment comment);

}
