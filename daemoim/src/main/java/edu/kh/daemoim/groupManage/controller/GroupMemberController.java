package edu.kh.daemoim.groupManage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.daemoim.groupManage.dto.GroupManageDto;
import edu.kh.daemoim.groupManage.dto.GroupMemberManageDto;
import edu.kh.daemoim.groupManage.dto.ManagePagination;
import edu.kh.daemoim.groupManage.mapper.GroupManageMapper;
import edu.kh.daemoim.groupManage.service.GroupManageService;
import edu.kh.daemoim.groupManage.service.GroupMemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("groupMemberManage")
@RequiredArgsConstructor
public class GroupMemberController {
	
	private final GroupMemberService service;
	private final GroupManageService groupService;
	
	/** 모임 멤버관리페이지로 이동
	 * @param groupNo : 모임번호
	 * @param model : 모임에 가입된 회원들을 담을 객체
	 * @return
	 */
	@GetMapping("{groupNo:[0-9]+}/memberManage")
	public String memberManage(
			@PathVariable("groupNo") int groupNo,
			@RequestParam Map<String, Object> paramMap,
			Model model) {
		
		paramMap.put("groupNo", groupNo);
		// 모임 멤버조회 호출
		Map<String, Object> map = service.getMemberList(paramMap);
		
		List<GroupMemberManageDto> memberList = (List<GroupMemberManageDto>)map.get("memberList");
		int memberAllCount = (int)map.get("memberAllCount");
		ManagePagination pagination = (ManagePagination)map.get("pagination");
		
		// 그룹 정보 담기
		GroupManageDto group = groupService.selectGroup(groupNo);
		model.addAttribute("group", group);
		model.addAttribute("memberList", memberList);
		model.addAttribute("memberAllCount", memberAllCount);
		model.addAttribute("pagination", pagination);
		
		return "groupManage/manageGroupMember";
	}
	
	/** 모임 가입신청 관리
	 * @param groupNo : 모임번호
	 * @param model : 모임에 가입된 회원들을 담을 객체
	 * @return
	 */
	@GetMapping("{groupNo:[0-9]+}/inviteManage")
	public String inviteManage(
			@PathVariable("groupNo") int groupNo,
			@RequestParam Map<String, Object> paramMap,
			Model model) {
		
		paramMap.put("groupNo", groupNo);
		// 가입신청목록 조회 호출
		Map<String, Object> map = service.getInviteList(paramMap);
		
		List<GroupMemberManageDto> memberList = (List<GroupMemberManageDto>)map.get("memberList");
		int memberAllCount = (int)map.get("memberAllCount");
		ManagePagination pagination = (ManagePagination)map.get("pagination");
		
		// 그룹 정보 담기
		GroupManageDto group = groupService.selectGroup(groupNo);
		model.addAttribute("group", group);
		model.addAttribute("memberList", memberList);
		model.addAttribute("memberAllCount", memberAllCount);
		model.addAttribute("pagination", pagination);
		
		return "groupManage/manageInvite";
	}
	
	
	/** 모임장 위임하기
	 * @param memberNo
	 * @param referer
	 * @return
	 */
	@PostMapping("changeLeader")
	public String changeLeader(
			@RequestParam("nextLeader") int memberNo,
			@RequestHeader("referer") String referer) {
		
		int gourpNo = Integer.parseInt( referer.split("/")[4] );
		// http://localhost/groupMemberManage/1/memberManage
		
		GroupManageDto newGroup = GroupManageDto.builder().groupNo(gourpNo).memberNo(memberNo).build();
		
		int result = service.changeLeader(newGroup);
		
		String path = "redirect:/";
		if(result < 1) {
			path += "groupMemberManage/" + gourpNo + "/memberManage";
		} else {
			/* 모임 메인화면으로 */
			System.out.println("성공한건가?");
		}
		
		return path;
	}
	
	/** 차단회원 관리페이지로 이동
	 * @param groupNo 전달받은 모임번호
	 * @param model
	 * @return 차단회원 관리페이지
	 */
	@GetMapping("{groupNo:[0-9]+}/banManage")
	public String gotobanManage(
			@PathVariable("groupNo") int groupNo,
			@RequestParam Map<String, Object> paramMap,
			Model model) {
		/* 그룹정보 group
		 * 차단회원 명수 memberAllCount
		 * 차단회원 리스트 memberList
		 */
		paramMap.put("groupNo", groupNo);
		Map<String, Object> map = service.gotobanManage(paramMap);
		
		int memberAllCount = (int)map.get("memberAllCount");
		List<GroupMemberManageDto> memberList = (List<GroupMemberManageDto>) map.get("memberList");
		ManagePagination pagination = (ManagePagination) map.get("pagination");
		
		GroupManageDto group = groupService.selectGroup(groupNo);
		model.addAttribute("group", group);
		model.addAttribute("memberAllCount", memberAllCount);
		model.addAttribute("memberList", memberList);
		model.addAttribute("pagination", pagination);
		
		return "groupManage/manageBan";
	}
	
}
