package edu.kh.daemoim.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.board.service.BoardService;
import edu.kh.daemoim.board.service.EditBoardService;
import edu.kh.daemoim.myPage.dto.MyPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("editBoard")
public class EditBoardController {

	private final EditBoardService service;

	private final BoardService boardService;

	/**
	 * 게시글 작성 화면으로 전환
	 * 
	 * @param groupNo
	 * @param boardTypeCode
	 * @return
	 */
	@GetMapping("{groupNo:[0-9]+}/{boardTypeCode:[0-9]+}/insert")
	public String boardInsert(
		@PathVariable("groupNo") int groupNo, 
		@PathVariable("boardTypeCode") int boardTypeCode) {

		if (boardTypeCode == 3)
			return "/board/imageAlbumWrite";
		else
			return "/board/boardWrite";
	}

	/**
	 * 게시글 등록하기
	 * 
	 * @param groupNo
	 * @param boardTypeCode
	 * @param inputBoard
	 * @param loginMember
	 * @param images
	 * @param ra
	 * @return
	 */
	@PostMapping("{groupNo:[0-9]+}/{boardTypeCode:[0-9]+}/insert")
	public String boardInsert(
			@PathVariable("groupNo") int groupNo, 
			@PathVariable("boardTypeCode") int boardTypeCode,
			@ModelAttribute Board inputBoard, 
			@SessionAttribute("loginMember") MyPage loginMember, 
			RedirectAttributes ra) {

		inputBoard.setMemberNo(loginMember.getMemberNo());

		int result = service.boardInsert1(inputBoard);
		int boardNo = inputBoard.getBoardNo();
		String path = null;
		String message = null;
		if (result == 0) {
			path = "/editBoard/" + groupNo + "/" + boardTypeCode + "/insert";

			message = "게시글작성을 실패하였습니다";
		} else {
			path =  "/board/" + groupNo + "/" + boardTypeCode + "/" + boardNo;
			message = "게시글이 작성되었습니다";
		}
		ra.addFlashAttribute("message", message);

		return "redirect:" + path;
	}
	
	@ResponseBody
	@PostMapping("{groupNo:[0-9]+}/{boardTypeCode:[0-9]+}/insert2")
	public String boardInsert2(
			@PathVariable("groupNo") int groupNo, 
			@PathVariable("boardTypeCode") int boardTypeCode,
			@ModelAttribute Board inputBoard, 
			@SessionAttribute("loginMember") MyPage loginMember,
			@RequestParam("images") List<MultipartFile> images, 
			RedirectAttributes ra) {

		inputBoard.setMemberNo(loginMember.getMemberNo());

		int boardNo = service.boardInsert(inputBoard, images);
		String path = null;
		String message = null;
		if (boardNo == 0) {
			message = "게시글작성을 실패하였습니다";
		} else {
			path =  "/board/" + groupNo + "/" + boardTypeCode + "/" + boardNo;
			message = "게시글이 작성되었습니다";
		}
		ra.addFlashAttribute("message", message);

		return path;
	}
	
	/** 게시글 삭제
	 * - DB에서 boardNo, memberNo가 일치하는
	 * 	 BOARD_TABLE의 행의 BOARD_DEL_FL 컬럼 값을 'Y'로 변경
	 * @param boardNo
	 * @param loginMember
	 * @param ra
	 * @param referer : 현재 컨트롤러 메서드를 요청한 페이지 주소
	 * 									(이전 페이지 주소)
	 * @return
	 *  - 삭제 성공 시 : "삭제 되었습니다" 메시지 전달
	 *  									+ 해당 게시판 목록으로 redirect
	 *  - 삭제 실패 시 : "삭제 실패" 메시지 전달
	 *  									+ 삭제 하려던 게시글 상세조회 페이지 redirect
	 */
	@PostMapping("delete")
	public String boardDelete(
		@RequestParam("boardNo") int boardNo,
		@SessionAttribute(value="loginMember", required=false) MyPage loginMember,
		RedirectAttributes ra,
		@RequestHeader("referer") String referer) {
		
		int result = service.boardDelete(boardNo, loginMember);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "삭제 되었습니다";
			path = "/board/" + referer.split("/")[4];
		} else {
			message = "삭제 실패";
			path = referer;
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	/** 게시글 수정 화면 전환
	 * @param groupNo : 모임 종류
	 * @param boardTypeCode : 게시판 종류
	 * @param boardNo : 수정할 게시글 번호
	 * @param loginMember : 로그인한 회원 정보(session)
	 * @param ra : redirect 시 request scope로 데이터 전달
	 * @param model : forward 시 request scope로 데이터 전달
	 * @return
	 */
	@PostMapping("{groupNo:[0-9]+}/{boardTypeCode:[0-9]+}/{boardNo:[0-9]+}/updateView")
	public String updateView(
		@PathVariable("groupNo") int groupNo,
		@PathVariable("boardTypeCode") int boardTypeCode,
		@PathVariable("boardNo") int boardNo,
		@SessionAttribute("loginMember") MyPage loginMember,
		RedirectAttributes ra,
		Model model) {
		
		// groupNo, boardTypeCode, boardNo가 일치하는 글 조회
		Map<String, Integer> map = Map.of("groupNo", groupNo, "boardTypeCode", boardTypeCode, "boardNo", boardNo);
		
		Board board = boardService.selectDetail(map);
		
		// 게시글이 존재하지 않는 경우
		if(board == null) {
			ra.addFlashAttribute("message", "해당 게시글이 존재하지 않습니다.");
			return "redirect:/board/" + groupNo + "/" + boardTypeCode; // 게시글 목록
		}
		
		// 게시글 작성자가 로그인한 회원이 아닌 경우
		if(board.getMemberNo() != loginMember.getMemberNo()) {
			ra.addFlashAttribute("message", "글 작성자만 수정 가능합니다");
			
			return String.format("redirect:/board/%d/%d/%d", groupNo, boardTypeCode, boardNo); // 상세 조회 주소
		}
		
		// 게시글이 존재하고 로그인한 회원이 작성한 글이 맞을 경우
		// 수정 화면으로 forward
		model.addAttribute("board", board);
		
		if(boardTypeCode == 3) return "/board/imageAlbumUpdate";
		else return "/board/boardUpdate";
	}
	
	/** 게시글 수정
	 * @return
	 */
	@PostMapping("{groupNo:[0-9]+}/{boardTypeCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(
		@PathVariable("groupNo") int groupNo,
		@PathVariable("boardTypeCode") int boardTypeCode,
		@PathVariable("boardNo") int boardNo,
		@ModelAttribute Board inputBoard,
		@SessionAttribute("loginMember") MyPage loginMember,
		@RequestParam("images") List<MultipartFile> images,
		@RequestParam(value="deleteOrderList", required = false) String deleteOrderList,
		RedirectAttributes ra) {
		
		// 1. 커맨드 객체 inputBoard에 로그인한 회원 번호 추가
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// inputBoard에 세팅된 값
		// : groupNo, boardTypeCode, boardNo, boardTitle , boardContent, memberNo
		
		
		// 2. 게시글 수정 서비스 호출 후 결과 반환
		int result = service.boardUpdate(inputBoard, images, deleteOrderList);
		
		String message = null;
		if(result > 0) {
			message = "게시글이 수정 되었습니다";
		} else {
			message = "수정 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		String path = String.format("redirect:/board/%d/%d/%d", groupNo, boardTypeCode, boardNo);
		
		
		return path; // 상세 조회
	}


}
