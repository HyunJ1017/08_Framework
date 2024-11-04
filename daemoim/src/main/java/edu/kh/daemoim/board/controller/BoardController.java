package edu.kh.daemoim.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.daemoim.board.service.BoardService;
import edu.kh.daemoim.board.dto.Board;
import edu.kh.daemoim.board.dto.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {
	
	private final BoardService service;
	
	/** 게시글 목록 조회
	 * @param boardTypeCode : 게시판 종류 번호
	 * @param cp : 현재 조회하려는 목록의 페이지 번호
	 * 						 (필수 아님, 없으면 1)
	 * @param model : forward 시 데이터 전달하는 용도의 객체(request)
	 */
	@GetMapping("{boardTypeCode:[0-9]+}")
	public String selectBoardList(
		@PathVariable("boardTypeCode") int boardTypeCode,
		@RequestParam(value = "cp", required = false, defaultValue = "1") int cp,
		Model model) {
		
		Map<String, Object> map = service.selectBoardList(boardTypeCode, cp);
	
		List<Board> boardList = (List<Board>)map.get("boardList");
		Pagination pagination = (Pagination)map.get("pagination");
		
		model.addAttribute("boardList", boardList);
		model.addAttribute("pagination", pagination);
		
		return "/board/boardList";
	}
	
}