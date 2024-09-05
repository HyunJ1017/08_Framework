package edu.kh.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.service.UserService;

@Controller	// @Controller역할 명시 + bean 등록
@RequestMapping("user")	// /user로 시작하는 요청 매핑
public class UserController {
	
	// 필드
	
		// 서비스객체생성 DI(의존성주입)
		@Autowired
		private UserService service;
		
	
	/** 사용자 번호를 입력받아 일치하는 사용자의 이름 조회
	 * @param userNo : 제출된 파라미터 중  key값이 "userNo"
	 * @param model  : Spring에서 사용하는 데이터전달용 객체
	 * @return
	 */
	@GetMapping("test1")
	public String selectUserName(
			@RequestParam("userNo") int userNo,
			Model model
			) {
		
		// 사용자 이름조회 서비스 호출 후 결과반환받기
		String userName = service.selectUserName(userNo);
		
		// 조회결과를 모델에 추가
		model.addAttribute("userName", userName);
		
		// Classpath:/templates/user/searchName.html forward
		return "user/searchName";
	}
	
	

}
