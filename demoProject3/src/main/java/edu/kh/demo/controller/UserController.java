package edu.kh.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.demo.dto.UserDto;
import edu.kh.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Controller	// @Controller역할 명시 + bean 등록
@RequestMapping("user")	// /user로 시작하는 요청 매핑
public class UserController {
	
	// 필드
	
		// 서비스객체생성 DI(의존성주입)
		// 등록되어있는 bean 중에서 같은 자료형을 주입해줌
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
	
	/** 사용자 전체 조회
	 * @param model : 데이터 전달용 객체
	 * @return user/selectAll.html forward
	 */
	@GetMapping("selectAll")
	public String getMethodName(Model model) {
		
		// service : 의존성주입(DI)받은 UserServiceImpl Bean 객체
		List<UserDto> userList = service.selectAll();
		
		model.addAttribute("userList", userList);
		
		return "user/selectAll";
	}
	
	
	/* RedirectAttributes ra
	 * - 리다이렉트 시 request scope로 값을 전달할 수 있는
	 *   스프링 제공 객체
	 *   
	 *   [원리]
	 *   
	 *   1) 값 세팅(request scope)
	 *   
	 *   2) 리다이렉트 수행되려고 할 때
	 *      1)에서 세팅된 값을 session scope로 잠깐() 대피
	 *      
	 *   3) 리다이렉트 수행 수
	 *       session에 대피했던 값을 다시 
	 *       request scope로 가져옴
	 */
	// @GetMapping("/example/ex3/1")
	// 주소 중 값을 얻어오고 싶은 부분을 {}로 작성하고
	// {}를 지칭하는 이름(==참조명) 내부에 작성
			// @PathVariable
			//  - 주소 중 일부분을 변수 값 처럼 사용
			//  + 해당 어노테이션으로 얻어온 값은
			//    현재 메서드 + forward한 html 파일에서 사용 가능
	/** userNo가 일치하는 User 조회
	 * @param userNo : 주소에 작선된 사용자 번호
	 * @param model : 데이터전달용객체
	 * @return
	 */
	@GetMapping("select/{userNo}")
	public String selectUser(
			@PathVariable("userNo") int userNo,
			Model model,
			RedirectAttributes ra
			) {
		
		UserDto user = service.selectUser(userNo);
		if(user!=null) {//조회결과가 있을경우
			model.addAttribute("user", user);
			return "user/selectUser";
		}
		
		//조회결과가 없을경우
		
			// 리다이렉트 시 잠깐 seeeion으로 대피할 값 추가
			ra.addFlashAttribute("message", "사용자가 존재하지 않습니다.");
		
		//목록으로 redirect
		return "redirect:/user/selectAll";
	}
	
	
	/* @ModelAttribute
	 * - 전달된 파라미터의 key(name 속성) 값이
	 *   작성된 DTO의 필드명과 일치하면
	 *   DTO 객체의 필드에 자동으로 세팅하는 어노테이션
	 *   --> 이렇게 만들어진 객체를 "커맨드 객체" 라고함
	 */
	
	/** 사용자 정보 수정
	 * @param userNo : 주소에 포함된 userNo
	 * @param user : userNo, userPw, userName 포함된 커맨드 객체
	 * @param ra : 리다이렉트 시 request scope로 값 전달하는 객체
	 * @return
	 */
	@PostMapping("update/{userNo}")
	public String update(
			@PathVariable("userNo") int userNo,
			@ModelAttribute UserDto user,
			RedirectAttributes ra
			) {
		log.debug("userNo : {}", userNo);
		log.debug("user   : {}", user);
		// 제출된 userPw, userName + @PathVariable("userNo") int userNo
		
		// DML 수행 결과 == 결과 행의 개수 == int 타입
		int result = service.updateUser(user);
		
		// 수정 결과에 따라 메시지 지정
		String message = null;
		if(result>0) message = "수정성공";
		else				 message = "수정실패";
		
		// ra에 값 추가하기
		ra.addFlashAttribute("message", message);
		
		// 상세조회 페이지로 다시 재요청redirect
		return "redirect:/user/select/"+userNo;
	}
	
	
	/** 유저 삭제하기
	 * @param userNo
	 * @param ra
	 * @return
	 */
	@PostMapping("/delete/{userNo}")
	public String deleteUser(
			@PathVariable("userNo") int userNo,
		  RedirectAttributes ra
			) {
		
		int result = service.deleteUser(userNo);
		
		// 삭제 여부에 따라 redirect 경로, 메시지 지정하기
		String path = null;
		String message = null;
		
		if(result>0) {
			path = "redirect:/user/selectAll";
			message = userNo + "번 유저가 삭제되었습니다.";
		} else {
			path = "redirect:/user/select/" + userNo;
			message = "삭제실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	/** 사용자 추가 화면으로 전환
	 * @return
	 */
	@GetMapping("insert")
	public String getMethodName() {
		return "user/insertUser";
	}
	
	
	/** 유저 회원가입
	 * @param user
	 * @param ra
	 * @return
	 */
	@PostMapping("insert")
	public String insertUser(
			@ModelAttribute UserDto user,
			RedirectAttributes ra
			) {
		
		int result = service.insertUser(user);
		
		String path = null;
		String message = null;
		
		if(result>0) {
			path = "redirect:/user/selectAll";
			message = user.getUserId() + " 추가 성공";
		} else {
			path = "redirect:/user/insert";
			message = "추가 실패";
			ra.addAttribute("user", user);
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	
}
