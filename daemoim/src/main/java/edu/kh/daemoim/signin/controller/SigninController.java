package edu.kh.daemoim.signin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.kh.daemoim.signin.service.SigninService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SigninController {

	private final SigninService service;
	
	@GetMapping("/signin")
	public String signup() {
		return "signin/signin";
	}
	
	
}
