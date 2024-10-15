package edu.kh.daemoim.signup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import edu.kh.daemoim.signup.service.SigninService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {
	
	private final SigninService service;

	
	@GetMapping("/signup")
	public String signup() {
		return "signup/signup";
	}
	
}
