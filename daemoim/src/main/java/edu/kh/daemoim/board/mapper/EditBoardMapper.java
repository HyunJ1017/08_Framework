package edu.kh.daemoim.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.board.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/** 게시글 부분(제목, 내용, 작성자, 게시판 종류) DB에 추가
	 * @param inputBoard
	 * @return
	 */
	int boardInsert(Board inputBoard);

	
	/** 여러이미지 한 번에 DB에 추가
	 * @param uploadList
	 * @return
	 */
	int insertUploadList(List<BoardImg> uploadList);


	/** 게시글 삭제
	 * @param boardNo
	 * @param memberNo
	 * @return result
	 */
	int boardDelete(@Param("boardNo") int boardNo, @Param("memberNo") int memberNo);
	 
	/** 게시글 부분만 수정(제목/내용)
	 * @param inputBoard
	 * @return result
	 */
	int boardUpdate(Board inputBoard);

	/** 기존에 존재하던 이미지 DB에서 삭제
	 * @param deleteOrderList
	 * @param boardNo
	 * @return result
	 */
	int deleteImage(@Param("orders") String deleteOrderList, @Param("boardNo") int boardNo);

	/** 이미지 1행 수정
	 * @param img
	 * @return result
	 */
	int updateImage(BoardImg img);

	/** 새로운 이미지 1행 삽입
	 * @param img
	 * @return result
	 */
	int insertImage(BoardImg img);


	List<BoardImg> selectImageList(int boardNo);
	
}
