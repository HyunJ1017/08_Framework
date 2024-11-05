package edu.kh.daemoim.groupMain.controller;

import java.lang.reflect.Member;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import edu.kh.daemoim.common.interceptor.GroupHeaderImgInterceptor;
import edu.kh.daemoim.groupMain.dto.Notice;
import edu.kh.daemoim.groupMain.dto.PhotoBox;
import edu.kh.daemoim.groupMain.dto.Schedule;
import edu.kh.daemoim.groupMain.service.GroupMainService;
import edu.kh.daemoim.myPage.dto.MyPage;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("groupMain")
// @Configuration
@SessionAttributes("loginMember") // 세션에서 loginMember 사용 선언

public class GroupMainController {

	private final GroupMainService service;

	@Autowired
	private GroupHeaderImgInterceptor groupHeaderImgInterceptor;

	/**
	 * 페이지 전환
	 * 
	 * @param model
	 * @return
	 */

	@GetMapping("{groupNo}")
	public String groupMain(Model model, @SessionAttribute(value = "loginMember", required = false) MyPage loginMember,
			@PathVariable("groupNo") int groupNo) {

	    // 로그인 여부 확인
        boolean memberGroupCheck = false;
        if (loginMember != null) {
            // 모임에 가입된 회원인지 확인
            memberGroupCheck = service.checkLoginMember(groupNo, loginMember.getMemberNo());
            model.addAttribute("memberGroupCheck", memberGroupCheck);
        } else {
            model.addAttribute("memberGroupCheck", false);
        }
        
		// 로그인한 경우 페이지에 표시할 데이터 가져오기
		// 공지사항 목록을 미리 불러와서 model에 추가
		List<Notice> boardList = service.getBoardList();
		List<PhotoBox> photos = service.getPhotos(groupNo);
		List<Schedule> schedules = service.getSchedule(groupNo);
		String introduces = service.getIntroduce(groupNo);

		model.addAttribute("boardList", boardList); // 모델에 추가하여 템플릿에서 사용
		model.addAttribute("photos", photos);
		model.addAttribute("schedules", schedules);
		model.addAttribute("introduces", introduces.split("\n"));

		return "groupMain/main"; // 뷰 이름 반환
	}

	/**
	 * 공지사항 목록 불러오기
	 * 
	 * @return
	 */
	@GetMapping("boardList")
	@ResponseBody // JSON으로 응답
	public List<Notice> selectBoardList(@RequestParam("groupNo") int groupNo) {
		System.out.println("=======================================");
		return service.selectBoardList(groupNo);
	}

	/**
	 * 모임 가입신청 확인 버튼 클릭 시
	 * @param groupNo
	 * @param loginMember
	 * @return
	 */
	@PostMapping("joinGroup/{groupNo}")
	@ResponseBody
	public String joinGroup( @PathVariable("groupNo") int groupNo,
			@SessionAttribute(value = "loginMember", required = false) MyPage loginMember) {
	

	if(loginMember == null) {
		return "로그인을 해주세요."; // 로그인 상태가 아닌 경우
	}
	
    boolean success = service.joinGroup(groupNo, loginMember.getMemberNo());
    return success ? "success" : "fail";
	
	}
	
}
