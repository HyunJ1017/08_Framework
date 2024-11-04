package edu.kh.daemoim.board.controller;

import org.springframework.stereotype.Controller;

import edu.kh.daemoim.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService service;

}
