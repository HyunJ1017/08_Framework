package edu.kh.daemoim.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	
	@RequestMapping("board")
	public String boardPage() {
		return "/board/boardList";
	}
	
	@GetMapping("boardSchedule")
	public String boardSchedulePage() {
		return "/board/boardSchedule";
	}
	
}