package edu.kh.daemoim.siteManage.controlller;

import java.io.Console;
import java.util.List;
import java.util.Map;

import org.apache.catalina.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.daemoim.siteManage.dto.StopMember;
import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.siteManage.service.SiteManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("siteManage")
@RequiredArgsConstructor
@Slf4j
public class SiteManageController {

	private final SiteManageService service;
  
  
	@GetMapping("main")
	public String siteManage(Model model
			) {


		// 서비스 호출 후 결과 받아오기
		Map<String, Object> map = service.getSiteManage();

		// "groupAllcount" : 총 모임 수
		// "memberAllcount" : 총 회원수
		// "outMemberCount" : 정지된 계정 수
		// "todayEnrollCount" : 오늘 새로 가입한 회원 수
		Map<String, Integer> countList = (Map<String, Integer>) map.get("countList");

		// 모임조회 - 전체활동이 많았던 모임 5개
		List<GroupManageDto> groupList = (List<GroupManageDto>) map.get("groupList");

		// 회원조회 - 최근 가입한 회원 5개
		List<GroupMemberManageDto> memberList = (List<GroupMemberManageDto>) map.get("memberList");

		// 신고 목록 조회
		List<StopMember> reportList = (List<StopMember>) service.getReportList();

		model.addAttribute("countList", countList);
		model.addAttribute("groupList", groupList);
		model.addAttribute("memberList", memberList);
		model.addAttribute("reportList", reportList);

		return "siteManage/main";
	}

	/**
	 * 계정 정지
	 * 
	 * @param email
	 * @param days
	 * @param reason
	 * @param model
	 * @return
	 */
	@PostMapping("/suspend")
	public String stopMember(@RequestParam("email") String email, @RequestParam("years") int years,
			@RequestParam("reason") String reason, RedirectAttributes ra, Model model) {

		// 이메일로 회원 찾기
		StopMember member = service.findMemberByEmail(email);

		if (member != null) {

			// 계정 정지처리
			member.setStopTerm(years);
			member.setStopReason(reason);

			int result = service.StopMember(member);

			ra.addFlashAttribute("message", years + "일 동안 정지되었습니다");

		} else {
			ra.addFlashAttribute("message", "올바른 이메일을 입력해주세요");
		}

		return "redirect:/siteManage/main#suspend";
	}

	/**
	 * 회원 탈퇴
	 * 
	 * @param email
	 * @param reason
	 */
	@PostMapping("/resign")
	public String resignMember(@RequestParam("email") String email, @RequestParam("reason") String reason,
			RedirectAttributes ra) {

		// 이메일로 회원 찾기
		StopMember member = service.findMemberByEmail2(email);

		if (member != null) {
			member.setStopReason(reason);

			int result = service.resignMember(member);
			ra.addFlashAttribute("message", "탈퇴 완료 되었습니다.");
		} else {
			ra.addFlashAttribute("message", "올바른 이메일을 입력해주세요.");
		}

		return "redirect:/siteManage/main#withdrawal";
	}

	/**
	 * 신고목록 불러오기
	 * 
	 * @param reportNo
	 * @return
	 */

	@GetMapping("/detail/{reportListNo}")

	public ResponseEntity<StopMember> getreportDetail(@PathVariable("reportListNo") int reportListNo) {

		StopMember reportDetail = service.getReportDetail(reportListNo);

		if (reportDetail != null) {
			return ResponseEntity.ok(reportDetail); // 보고서가 존재하는 경우 200 OK 응답
		} else {
			return ResponseEntity.notFound().build(); // 보고서가 존재하지 않을 경우 404 Not Found 응답
		}

	}

	/**
	 * 신고목록 삭제
	 * 
	 * @param reportNo
	 */
	@GetMapping("/delete/{reportListNo}")
	@ResponseBody
	public void deleteReport(@PathVariable("reportListNo") int reportListNo) {

		service.deleteReportOut(reportListNo);
	}

}
