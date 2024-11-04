package edu.kh.daemoim.signup.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.daemoim.signup.service.SignupService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SignupController {
	
	private final SignupService service;

	
	@GetMapping("/signup")
	public String signupPage() {
		return "signup/signup";
	}
	
	@PostMapping("signup")
	public String signupMember() {
		
		return null;
	}
	
	@ResponseBody // 반환 값을 응답 본문 (ajax 코드)로 반환
	@GetMapping("emailCheck")
	public int emailCheck(
			@RequestParam("email") String email
			) {
		return service.emailCheck(email);
	}
	
//	아이디
	@ResponseBody
	@GetMapping("idCheck")
	public int idCheck(@RequestParam("memberId") String memberId) {
		return service.memberIdCheck(memberId);
	}
	
//	닉네임 
	@ResponseBody
	@GetMapping("nicknameCheck")
	public int nicknameCheck(@RequestParam("nickname") String nickname) {
		return service.nicknameCheck(nickname);
	}
}
