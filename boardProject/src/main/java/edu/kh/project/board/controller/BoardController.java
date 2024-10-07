package edu.kh.project.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.dto.Board;
import edu.kh.project.board.dto.Pagination;
import edu.kh.project.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j // 로그 남기기용
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

	private final BoardService service;
	
	/** 게시글 목록 조회
	 * @param boardCode : 게시판 종류 번호
	 * @param model : forward시 데이터 전달하는 용도의 객체
	 * @param cp : 현재 조회하려는 목록의 페이지 번호
	 *             (필수 아님, 없으면 1)
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(
			@PathVariable("boardCode") int boardCode,
			@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
			Model model) {
		
		// 서비스 호출 후 결과 반환
		// 목록 조회인데 Map 으로 반환 받는 이유
		// - 서비스에서 여러 결과를 묶어서 받아와야 해서
		// -- 메서드는 결과를 1개만 반환할 수 있음
		Map<String, Object> map = service.selectBoardList(boardCode, cp);
		
		// map에 묶여있는 값 풀어놓기
		List<Board> boardList = (List<Board>) map.get("boardList");
		Pagination pagination = (Pagination) map.get("pagination");
		
		// 정상 조회되었는지 log 확인
//		for(Board b : boardList) log.debug(b.toString());
//		log.debug(pagination.toString());
		model.addAttribute("boardList", boardList);
		model.addAttribute("pagination", pagination);
		
		return "board/boardList";
	}
	
	/** 게시글 상세 조회
	 * @param boardCode : 게시판 종류
	 * @param boardNo   : 게시글 번호
	 * @param model     : forward 시 request scope 값 전달 객체
	 * @param ra				: redirect시 request scope 값 전달 객체
	 * @return
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}")
	public String boardDetail(
			@PathVariable("boardCode") int boardCode,
			@PathVariable("boardNo") int boardNo,
			Model model,
			RedirectAttributes ra
			) {
		
		// 1. sql 수행에 필요한 파라미터들 Map으로 묶기
		Map<String, Integer> map
			= Map.of("boardCode", boardCode,
							 "boardNo", boardNo);
		
		// 2) 서비스 호출 후 결과 반환받기
		Board board = service.selectDetail(map);
		
		model.addAttribute("board", board);
		
		// 게시판 이미지를 출력하기위한 이미지리스트의 이미지 시작번호(썸네일 구분)
		int start = 0;
		
		// 조회된 이미지 목록이 있을 경우
		if ( board.getImageList().isEmpty() == false) {
			// 썸네일이 있을 경우
			if(board.getThumbnail() != null) {
				start = 1;
			}
		}
		
		model.addAttribute("start", start);
		
		return "board/boardDetail";
	}
	
	
	
	
}
