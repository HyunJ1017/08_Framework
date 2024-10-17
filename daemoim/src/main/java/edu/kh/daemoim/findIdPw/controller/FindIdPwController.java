package edu.kh.daemoim.findIdPw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.kh.daemoim.findIdPw.service.FindIdPwService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FindIdPwController {

	private final FindIdPwService service;
	
	@GetMapping("/findIdPw")
	public String findIdPw() {
		
		return "findIdPw/findIdPw";
	}
	
}
