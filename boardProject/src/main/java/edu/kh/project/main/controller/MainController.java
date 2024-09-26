package edu.kh.project.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.project.main.service.MainService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@SessionAttributes({"loginMember"})	// 중괄호 넣는거 까먹지 마세요
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
	
	/** 빠른 로그인
	 * @param memberNo : 전달받은 멤버넘버
	 * @param model
	 * @return
	 */
	@PostMapping("directLogin")
	public String directLogin(
			@RequestParam("memberNo") int memberNo,
			Model model) {
		
		Member loginMember = service.directLogin(memberNo);
		
		// 로그인된 회원 정보를 session에 추가
		model.addAttribute("loginMember", loginMember);
		
		return "redirect:/";
	}
	
	/** 비밀번호 초기화
	 * @param memberNo
	 * @return
	 */
	@ResponseBody
	@PostMapping("resetPw")
	public int resetPw(
			@RequestBody int memberNo) {
		return service.resetPw(memberNo);
	}
	
	/** 탈퇴여부 변경
	 * @param memberNo
	 * @return 1 성공 || 0 입력넘버없음
	 */
	@ResponseBody
	@PutMapping("changeStatus")
	public int changeStatus(@RequestBody int memberNo) {
		return service.changeStatus(memberNo);
	}
	
	
	
}
