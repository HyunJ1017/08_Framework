package edu.kh.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	// 컨트롤러(요청, 응답제어) 명시 + BEAN 등록
public class MainController {
	
	// == localhost/ 요청시 최상위 주소를 응답할 메서드
	@RequestMapping("/")
	public String mainPage() {
		
		// 장점 : JAVA를 거쳐서 메인페이지가 보여짐
		// -> 메인페이지에 추가 세팅값이나 DB 조회값을 위임할 HTML에서 출력할 수 있음
		
		// 사용하는 템플릿엔진 : Thymeleaf
		// Thymeleaf를 사용하는 프로젝트에서 forward시 제공하는
		// 접두사 : classfath:/emplates/ 
		// 접미사 : .html
		
		
		return "common/main";
	}
	
	
	

}
