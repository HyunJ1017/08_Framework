package edu.kh.daemoim.tacou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.kh.daemoim.tacou.service.TacouService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TacouController {

	private final TacouService service;
		
	// 약관 동의 페이지 요청
    @GetMapping("/tacou")
    public String showTacouPage() {
        return "tacou/tacou"; 
    }

    // 약관 1 페이지 요청
    @GetMapping("/tacou/tacou1")
    public String showTacou1() {
        return "tacou/tacou1"; 
    }

    // 약관 2 페이지 요청
    @GetMapping("/tacou/tacou2")
    public String showTacou2() {
        return "tacou/tacou2"; 
    }

    // 약관 3 페이지 요청
    @GetMapping("/tacou/tacou3")
    public String showTacou3() {
        return "tacou/tacou3"; 
    }
	
	}
	

