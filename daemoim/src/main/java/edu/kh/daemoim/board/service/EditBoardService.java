package edu.kh.daemoim.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.myPage.dto.MyPage;

public interface EditBoardService {

	/** 게시글 작성
	 * @param inputBoard
	 * @param images
	 * @return
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images);

	/** 게시글 삭제
	 * @param boardNo
	 * @param loginMember
	 * @return result
	 */
	int boardDelete(int boardNo, MyPage loginMember);

	/** 게시글 수정
	 * @param inputBoard
	 * @param images
	 * @param deleteOrderList
	 * @return
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList);

	int boardInsert1(Board inputBoard);

}
