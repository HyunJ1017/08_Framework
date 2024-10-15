package edu.kh.daemoim.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	@RequestMapping("/")
	public String mainPage() {
		return "common/main";
	}
	
}
