package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // 요청/응답 제어하는 Controller 역할 명시 + Bean 등록
public class MainController {

	@RequestMapping("/")
	public String mainPage() {

		// 포워드 위치, Thymeleaf 접미사, 접두사로 자동완성
		// classpath:/templetes/ "common/main" .html
		return "common/main";
	}
}
