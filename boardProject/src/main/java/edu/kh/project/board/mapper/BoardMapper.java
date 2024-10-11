package edu.kh.project.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.Comment;

@Mapper
public interface BoardMapper {

	/** boardCode가 일치하는 게시글 중 삭제되지 않은 게시글 수 조회하기
	 * @param boardCode
	 * @return
	 */
	int getListCount(int boardCode);

	/** 지정된 페이지 분량의 게시글 목록 조회
	 * @param boardCode
	 * @param rowBounds
	 * @return
	 */
	List<Board> selectBoardList(int boardCode, RowBounds rowBounds);

	/** 게시글 상세조회
	 * @param map
	 * @return
	 */
	Board selectDetail(Map<String, Integer> map);

	// 조회수 증가
	int updateReadCount(int boardNo);

	// 좋아요 눌렀는지 검사하기
	int checkBoardLike(@Param("boardNo") int boardNo, @Param("memberNo") int memberNo);
	// 좋아요 눌렀다 땟다 하기
	int insertBoardLike(@Param("boardNo") int boardNo, @Param("memberNo") int memberNo);
	int deleteBoardLike(@Param("boardNo") int boardNo, @Param("memberNo") int memberNo);
	// 좋아요 갯수 확인
	int getLikeCount(int boardNo);

	// 게시판 종류 조회
	List<Map<String, String>> selectBoardTypeList();

	// 댓글 목록 조회
	List<Comment> selectCommentList(int boardNo);

}
