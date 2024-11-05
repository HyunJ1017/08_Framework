package edu.kh.daemoim.myPage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.main.dto.MainDTO;
import edu.kh.daemoim.myPage.dto.MyPage;
import edu.kh.daemoim.myPage.service.MyPageService;
import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("myPage")
@SessionAttributes({"loginMember"})
public class MyPageController {

	private final MyPageService service;

	@GetMapping("info")
	public String info() {

	    return "myPage/myPage-info";
	}
	
	@GetMapping("memberInfo/{memberNickname}")
	public String memberInfo(
			@PathVariable("memberNickname") String memberNickname,
			@SessionAttribute("loginMember") MyPage loginMember,
			Model model) {
		
		MyPage userInfo = service.findMemberNickname(memberNickname);
		
        model.addAttribute("userInfo", userInfo);
		
		
		return "myPage/memberPage";
	}

	@GetMapping("changePw")
	public String changePw() {
		return "myPage/myPage-changePw";
	}

	@GetMapping("withdrawal")
	public String withdrawal() {
		return "myPage/myPage-withdrawal";
	}

	@GetMapping("withdrawalPage")
	public String withdrawalPage() {
		return "myPage/withdrawalPage";
	}

	@PostMapping("info")
	public String updateInfo(@ModelAttribute MyPage inputMember, 
			@SessionAttribute("loginMember") MyPage loginMember,
			RedirectAttributes ra) {

		int MemberNo = loginMember.getMemberNo();

		inputMember.setMemberNo(MemberNo);

		int result = service.updateInfo(inputMember);

		String message = null;
		if (result > 0) {
			message = "수정 되었습니다!!";

			loginMember.setMemberNickname(inputMember.getMemberNickname());
			loginMember.setMemberIm(inputMember.getMemberIm());

		} else
			message = "수정 실패하였습니다";

		ra.addAttribute("message", message);

		return "redirect:info";
	}

	@PostMapping("withdrawal")
	public String withdrawal(@RequestParam("memberPw") String memberPw,
			@SessionAttribute("loginMember") MyPage loginMember, 
			RedirectAttributes ra, SessionStatus status) {

		int result = service.withdrawal(memberPw, loginMember);

		String message = null;
		String path = null;

		if (result > 0) {
			message = "탈퇴 되었습니다";
			path = "/";
			status.setComplete();
		} else {
			message = "비밀번호가 일치 하지 않습니다";
			path = "withdrawal";

		}

		return "redirect:" + path;
	}

	// 닉네임 변경 중복 검사
	@ResponseBody
	@GetMapping("checkNickname")
	public int checkNickname(@RequestParam("input") String input) {

		return service.checkNickname(input);

	}

	@PostMapping("changePw")
	public String changePw(
		@RequestParam("currentPw") String currentPw,
		@RequestParam("newPw") String newPw,
		@SessionAttribute("loginMember") MyPage loginMember,
		RedirectAttributes ra
			) {
		
		// 서비스 호출 후 결과 반환 받기
		int result = service.changePw(currentPw, newPw, loginMember);
		
		
		String message = null;
		String path = null;
		
		// 결과에 따른 응답 제어
		if(result > 0) {
			message = "비밀번호가 변경 되었습니다";
			path = "info"; 
		} else {
			message = "현재 비밀번호가 일치하지 않습니다";
			path = "changePw"; 
		}
		
		ra.addFlashAttribute("message", message);
	
		return "redirect:" + path;
	}
	
	// 프로필 사진 변경
	@PostMapping("profile")
	public String profile(
			@RequestParam("profileImg") MultipartFile profileImg,
			@SessionAttribute("loginMember") MyPage loginMember,
			RedirectAttributes ra) {
		
		// 회원번로 얻어오기 
		int memberNo = loginMember.getMemberNo();
		
		String filePath = service.profile(profileImg, memberNo);
		
		String message = null;
		
		if(filePath != null) {
			message = "프로필 이미지가 변경 되었습니다";
			
			// 저장된 프로필 이미지 정보 동기화
			loginMember.setMemberImg(filePath);
			
		}else {
			message = "프로필 이미지 변경에 실패하였습니다";
		}
		
		ra.addFlashAttribute("message", message);
		
		
		return "redirect:info";
	}

	@GetMapping("myGroup")
	public String myGroup(
			HttpSession session,
			Model model) {
        MyPage loginMember = (MyPage) session.getAttribute("loginMember");


        if (loginMember != null) {
            int memberNo = loginMember.getMemberNo();
            List<MainDTO> joinGroups = service.findMyGroup(memberNo);
            model.addAttribute("joinGroups", joinGroups);
        }
		return "myPage/myGroup";
	}
	
	@PostMapping("/myGroup")
	@ResponseBody
	public Map<String, Object> outGroup(@RequestBody int groupNo, 
	                                    @SessionAttribute("loginMember") MyPage loginMember, 
	                                    RedirectAttributes ra) {
	    Map<String, Object> result = new HashMap<>();

	    int memberNo = loginMember.getMemberNo();
	    int groupOutCheck = service.groupOut(memberNo, groupNo);
	    if (groupOutCheck == 0) {
	        result.put("status", "error");
	        result.put("message", "모임 탈퇴에 실패했습니다.");
	    } else {
	        result.put("status", "success");
	        result.put("message", "모임에서 성공적으로 탈퇴했습니다.");
	    }
	    return result;
	}
    
	@GetMapping("memberPage/{memberNickname}")
	public String memberPage(){
		
		
		
		
		return "myPage/memberPage";
		
	}
	
}
	

