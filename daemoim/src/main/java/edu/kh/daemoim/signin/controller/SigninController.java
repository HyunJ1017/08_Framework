package edu.kh.daemoim.signin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.daemoim.myPage.dto.MyPage;
import edu.kh.daemoim.signin.service.SigninService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes({"loginMember"})
public class SigninController {

   private final SigninService service;
   
   @GetMapping("/signin")
   public String signup() {
      return "signin/signin";
   }
   
   @PostMapping("/login")
   public String login(@RequestParam("memberId") String memberId,
                       @RequestParam("memberPw") String memberPw,
                       @RequestParam(value = "saveId", required = false) String saveId, // 체크박스 값
                       RedirectAttributes ra,
                       Model model,
                       SessionStatus status,
                       HttpServletResponse resp) { // HttpServletResponse 추가

       try {
           MyPage loginMember = service.login(memberId, memberPw);

           // 로그인 실패 시
           if (loginMember == null) {
               ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
               return "redirect:/signin";
           } 
           // 탈퇴된 회원일 경우
           else if ("Y".equals(loginMember.getMemberDelFl())) {
               ra.addFlashAttribute("message", "탈퇴된 회원입니다. 다시 로그인할 수 없습니다.");
               status.setComplete();
               return "redirect:/signin"; // 로그인 페이지로 리다이렉트
           } 
           // 로그인 성공 후 권한 확인
           else {
               model.addAttribute("loginMember", loginMember);

               Cookie cookie = new Cookie("saveId", memberId); // 이메일을 저장할 쿠키 생성
               cookie.setPath("/");

               if (saveId == null) { // 체크 X, 쿠키 삭제
                   cookie.setMaxAge(0); // 쿠키 수명을 0으로 설정하여 삭제
               } else { // 체크 O
                   cookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 수명 설정 (30일)
               }

               resp.addCookie(cookie); // 쿠키를 클라이언트에 추가

               int authoritycheck = service.authoritycheck(memberId);

               // 관리자 권한일 경우
               if (authoritycheck == 3) {
                   return "redirect:/siteManage/main";
               } 
               // 일반 사용자일 경우
               else {
                   return "redirect:/";
               }
           }
       } catch (Exception e) {
           ra.addFlashAttribute("message", "로그인 중 오류가 발생했습니다.");
           return "redirect:/";
       }
   }

   @GetMapping("logout")
	public String logout(SessionStatus status) {
		
		/* sessionStatus
		 *  -@SessionAttributes를 이용해 등록된 객체(값)의 상태흫
		 *  관리하는 객체
		 *  
		 *  -SessionStatus.setComplete();
		 *  -> 세션상태 완료 == 없앰(만료)
		 * 
		 */
		 status.setComplete();
		
		return "redirect:/";// 메인페이지
	}
}