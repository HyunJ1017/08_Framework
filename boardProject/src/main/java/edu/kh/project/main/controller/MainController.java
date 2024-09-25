package edu.kh.project.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.main.service.MainService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller // 요청/응답 제어하는 Controller 역할 명시 + Bean 등록
public class MainController {
	
	private final MainService service; // 의존성주입 DI @RequiredArgsConstructor

	@RequestMapping("/")
	public String mainPage() {

		// 포워드 위치, Thymeleaf 접미사, 접두사로 자동완성
		// classpath:/templetes/ "common/main" .html
		return "common/main";
	}
	
	/** 멤버 리스트 불러오기
	 * @return memberList
	 */
	@ResponseBody
	@GetMapping("selectMemberList")
	public List<Member> selectMemberList(){
		return service.selectMemberList();
	}
	
	
}
