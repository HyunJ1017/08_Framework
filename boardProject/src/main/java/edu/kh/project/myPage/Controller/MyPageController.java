package edu.kh.project.myPage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.service.MyPageService;
import lombok.extern.slf4j.Slf4j;

/* @SessionAttribute"s" 용도
 * 1. Model을 이용하여 값을 request -> session으로 scope 변경
 * 2. @SessionAttribute (* 's' 안붙음)를 이용해 
 * 		@SessionAttribute"s"에 의해서 session에 등록된 값을 얻어올 수 있음 
 */

@Slf4j
@Controller
@RequestMapping("myPage")
@SessionAttributes({"loginMember"}) // memberController에서 등록한 loginMember를 얻어올수 있음
public class MyPageController {

	@Autowired	// 의존성주입(DI, Dependency Injection)
	private MyPageService service;
	
	/**
	 *  마이페이지(내 정보) 전환
	 * @param loginMember : session에 저장된 login 멤버
	 * @param model : 데이터 전달하는 용도의 '객체'
	 * @return
	 */
	@GetMapping("info")
	public String info(
			@SessionAttribute("loginMember") Member loginMember,
			Model model
			) {
		
		// 로그인회원정보의 주소가 있을경우
		if(loginMember.getMemberAddress() != null) {
			
			// 주소를 , 기준으로 쪼개서 String[] 형태로 반환
			String[] arr = loginMember.getMemberAddress().split(",");
			
			// "04540,서울 중구 남대문로 120,2층"
			// -> {"04540", "서울 중구 남대문로 120", "2층"}
			model.addAttribute("postCode", arr[0]);
			model.addAttribute("address", arr[1]);
			model.addAttribute("detailAddress", arr[2]);
		}
		
		return "myPage/myPage-info";
	}
	
	/**
	 *  내 정보 수정
	 * @param inputMember : 수정할 닉네임, 전화번호, 주소
	 * @param loginMember : 현재 로그인된 회원 정보
	 * 	session에 저장된 Member 객체의 주소가 반환됨
	 *  == session에 저장된 Member 객체의 데이터를 수정할 수 있음
	 * @return
	 */
	@PostMapping("info")
	public String updateInfo(
			@ModelAttribute Member inputMember,
			@SessionAttribute("loginMember") Member loginMember,	//-> session에 있는 loginMember를 얕은복사 해서, 수정하면 session에 있는것도 같이 수정할 수 있음
			// memberController에서 @SessionAttributes를 통해 등록한 loginMember를 얻어올수 있음
			// 사용 방법
			// 1) 클래스 위에 @SessionAttributes 어노테이션을 작성하고 해당 클래스에서 깨내 사용할 값의 key를 작성
			//    -> 그럼 세션에서 값을 미리 얻어와놈
			// 2) 필요한 메서드 매개변수에 @SessionAttribute("key")를 작성하면 해당 session값을 얻어와서 사용할 수 있게 해줌
			RedirectAttributes ra
			// redirect시 requestScope로 값 전달
			) {
		
		// 1. inputMember에 로그인회원의 회원번호를 추가
		int memberNo = loginMember.getMemberNo();
		inputMember.setMemberNo(memberNo); // 멤버번호 + 수정할 닉네임, 전화번호, 주소
		
		// 2. 화원정보수정 서비스 호출
		int result = service.updateInfo(inputMember);
		
		// 3. 수정 결과에 따라 message 지정
		String message = null;
		if(result > 0) {
			message = "수정 성공!!";
			
			//4. 수정 성공시 session에 저장된 login 회원 정보를 수정값으로 변경해서 DB와 같은 데이터를 가지게 함 == 동기화
			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberTel(inputMember.getMemberTel());
			loginMember.setMemberAddress(inputMember.getMemberAddress());
		}
		else					 message = "수정 실패..";
		
		ra.addFlashAttribute("message", message);
		// footer.html에서 수행
		
		
		
		return "redirect:info"; // /myPage/info GET방식 요청
	}
	
	/**(비동기) 닉네임 중복검사
	 * 
	 * @param input
	 * @return 0 | 1, 0이면 중복아님, 1중복맞음
	 */
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("input") String input) {
		return service.checkNickname(input);
	}
	
	/** 비밀번호 변경 사이드메뉴
	 * 클릭시 변경 화면 html로 포워드
	 * @return
	 */
	@GetMapping("changePw")
	public String changePw() {
		return "myPage/myPage-changePw";
	}
	
	/** 실제 비밀번호 변경 메서드
	 * @param currentPw : 현재 비밀번호
	 * @param newPw : 변경하려는 새 비밀번호
	 * @param loginMember : 세션에서 얻어온 로그인한 회원 정보
	 * @param ra :리다이렉트 시 request scope로 데이터를 전달하는 일회성 객체
	 */
	@PostMapping("changePw")
	public String changePw(
			@RequestParam("currentPw") String currentPw,
			@RequestParam("newPw") String newPw,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra,
			Model model
			) {
		
		// 서비스 호출 후 결과 반환받기
		int result = service.changePw(currentPw, newPw, loginMember);
		
		String message = null;
		String path = null;
		
		// 결과에 따른 응답제어
		if( result > 0 ) { // 성공시
			message = "비밀번호가 변경 되었습니다.";
			path = "info"; // 내정보 페이지로 리다이렉트
		} else { // 실패시
			message = "현재 비밀번호가 일치하지 않습니다.";
			path = "changePw"; // 비밀번호 변경 페이지로 리다이렉트
		}
		
		ra.addFlashAttribute("message", message);
		
		// 상대경로로 작성된 주소작성( "/"로 시작되지 않음 )
		// 현재 컨트롤러 매핑주소 : /myPage/changePw
		// 작성된 주소가 제출되는 형태 : /myPage/ + path
		return "redirect:" + path;
	}
	
	/**
	 * 회원 탈퇴 페이지로 전환 (GET)
	 * @return
	 */
	@GetMapping("secession")
	public String secession() {
		return "myPage/myPage-secession";
	}
	
	/**
	 * 회원 탈퇴 메서드 (POST)
	 * @param memberPw : 입력받은 비밀번호
	 * @param loginMember : session에 있는 로그인되어있는 회원의 정보
	 * @param ra : redirect시 request scope 데이터 전달
	 * @param status : @SessionAtrubutes로 관리되는 세션데이터의 상테제어(세션만료용, 로그아웃에도 씀) 
	 * @return
	 */
	@PostMapping("secession")
	public String secession(
			@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra,
			SessionStatus status
			) {
		
		// 서비스 호출 후 결과 받환받기
		int result = service.secession(memberPw, loginMember);
		
		String message = null;
		String path = null;
		
		if( result > 0 ) {
			message = "탈퇴 되었습니다.";
			path = "/"; // 메인페이지로 redirect
			status.setComplete(); // 세션 만료 -> 로그아웃
		} else {
			message = "비밀번호가 일치하지 않습니다.";
			path = "secession"; // 탈퇴페이지로 redirect
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	
	/** 프로필수정페이지로 이동
	 * @return
	 */
	@GetMapping("profile")
	public String prifile() {
		return "myPage/myPage-profile";
	}
	
	
	/** 로그인한 회원의 프로필 이미지 수정
	 * @param profileImg : 제출된 이미지
	 * @param loginMember : 로그인한 회원 정보
	 * @param ra : redirect시 request scope로 값 전달
	 * @return
	 */
	@PostMapping("profile")
	public String profile(
			@RequestParam("profileImg") MultipartFile profileImg,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra) {
		
		// 1) 로그인 한 회원의 PK
		int memberNo = loginMember.getMemberNo();
		// 2) 업로드 된 이미지로 프로필 이미지 변경하는 서비스 호출
		String filePath = service.profile(profileImg, memberNo);
		// 3) 응답처리
		String message = null;
		
		// 저장된 이미지 경로가 정상적으로 돌아오면
		message = "프로필 이미지가 변경되었습니다.";
		
		// DB랑 SESSION에 저장된 프로필이미지 동기화
		loginMember.setProfileImg(filePath);
			
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:profile";
	}
	
	
}
