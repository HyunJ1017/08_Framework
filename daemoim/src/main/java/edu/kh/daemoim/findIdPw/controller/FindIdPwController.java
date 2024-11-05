package edu.kh.daemoim.findIdPw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import edu.kh.daemoim.findIdPw.service.FindIdPwService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class FindIdPwController {

	private final FindIdPwService service;

	@GetMapping("/findIdPage")
	public String findIdPw() {

		return "findIdPw/findIdPage";
	}

	@GetMapping("/findPwPage")
	public String findPw() {

		return "findIdPw/findPwPage";
	}

	@ResponseBody
	@GetMapping("/findIdEmail")
	public int findIdEmailcheck(@RequestParam("email") String email) {
		return service.findIdEmailcheck(email);
	}

	@ResponseBody
	@GetMapping("/findPwEmail")
	public int findIdEmailcheck(@RequestParam("id") String id, @RequestParam("email") String email) {
	    // Database 확인 메서드 호출
	    return service.findPwEmailcheck(id, email); 
	}


	@GetMapping("/findIdPw/memberIdPage")
	public String memberIdPage(@RequestParam("memberId") String memberId, Model model) {
		/* System.out.println("Received memberId: " + memberId); 반환 확인용 */
		model.addAttribute("memberId", memberId);
		return "/findIdPw/memberIdPage";
	}

	@ResponseBody
	@PostMapping("/findIdPw/memberIdPage")
	public String memberId(@RequestBody String email) {

		return service.memberId(email);
	}

	@GetMapping("/findIdPw/findPwChangePage")
	public String memberPwChangePage(@RequestParam("findPwMemberId") String findPwMemberId, Model model) {
		model.addAttribute("findPwMemberId", findPwMemberId);
		return "/findIdPw/findPwChangePage";
	}

	@PostMapping("findIdPw/findPwChangePage")
	public String chagePwAuthKeyPage(
			@RequestParam("findPwMemberId") String findPwMemberId,
			@RequestParam("newPw") String newPw,
			RedirectAttributes ra) {
		
		String path = null;
		String message = null;
		
		int result = service.chagePwAuthKeyPage(findPwMemberId,newPw);
		
		if(result == 0) {
			
			path = "findPwChangePage";
			message = "변경에 실패하였습니다";
			
			
		}else {
			
			path = "/";
			message = "변경 성공";
			
		}
		
		ra.addFlashAttribute("message", message);
		
		
		
		return "redirect:" + path;
	}
	
	
	 
	 

}