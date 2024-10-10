package edu.kh.project.board.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.dto.Board;

public interface EditBoardService {

	/** 게시글 작성하기
	 * @param inputBoard
	 * @param images
	 * @return 삽입 완료된 게시글의 게시글 번호
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images);

	/** 게시글 삭제
	 * @param board
	 * @return
	 */
	int boardDelete(Board board);

	/** 게시글 수정
	 * @param inputBoard
	 * @param images
	 * @param deleteOrderList
	 * @return
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrderList);

}
