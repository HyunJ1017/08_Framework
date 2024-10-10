package edu.kh.project.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	
	/** 게시글 부분(제목, 내용, 작성자, 게시판종류) INSERT
	 * @param inputBoard
	 * @return result
	 */
	int boardInsert(Board inputBoard);
	
	/** 여러 이미지 한 번에 INSERT
	 * @param uploadList
	 * @return insertRows
	 */
	int insertUploadList(List<BoardImg> uploadList);

	/** 게시글 삭제
	 * @param board
	 * @return
	 */
	int boardDelete(Board board);

	/** 게시글 부분만 수정(제목/내용)
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	/** 기존에 존재하던 이미지 DB에서 삭제
	 * @param deleteOrderList
	 * @param boardNo
	 * @return
	 */
	int deleteImg(@Param("orders") String deleteOrderList, @Param("boardNo") int boardNo);

	/** 이미지 1행 수정
	 * @param img
	 * @return
	 */
	int updateImg(BoardImg img);

	/** 이미지 1행 삽입
	 * @param img
	 * @return
	 */
	int insertImg(BoardImg img);

}
