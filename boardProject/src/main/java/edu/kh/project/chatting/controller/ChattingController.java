package edu.kh.project.chatting.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import edu.kh.project.chatting.dto.ChattingRoom;
import edu.kh.project.chatting.dto.Message;
import edu.kh.project.chatting.service.ChattingService;
import edu.kh.project.member.dto.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@Controller
@RequestMapping("chatting")
@RequiredArgsConstructor
public class ChattingController {
	
	private final ChattingService service;
	
	/** 채팅 페이지로 이동
	 * @return
	 */
	@GetMapping("")
	public String chattingPage(
			@SessionAttribute("loginMember") Member loginMember,
			Model model) {
		List<ChattingRoom> roomList = service.selectRoomList(loginMember.getMemberNo());
		model.addAttribute("roomList", roomList);
		return "chatting/chatting";
	}
	
	/** 채팅 상대 검색
	 * @param query : 상대 닉네임 또는 이메일
	 * @param loginMember : 로그인한 회원 정보
	 * @return memberList
	 */
	@GetMapping("selectTarget")
	@ResponseBody
	public List<Member> selectTarget(
			@RequestParam("query") String query,
			@SessionAttribute("loginMember") Member loginMember){
		return service.selectTarget(query, loginMember.getMemberNo());
	}

	/** 채팅방 입장( 처음 채팅이면 채팅방 생성(insert))
	 * @param targetNo
	 * @param loginMember
	 * @return 두 회원이 포함된 채팅방 번호
	 */
	@PostMapping("enter")
	@ResponseBody
	public int chattingEnter(
			@RequestBody int targetNo,
			@SessionAttribute("loginMember") Member loginMember) {
		
		int chattingNo = service.chattingEnter(targetNo, loginMember.getMemberNo());
		
		return chattingNo;
	}
	
	/** 로그인 한 회원이 참여한 체팅방 목록 조회하기
	 * @param loginMember : 로그인한 회원
	 * @return chattingRoom List
	 */
	@ResponseBody
	@GetMapping("roomList")
	public List<ChattingRoom> selectRoomList(
			@SessionAttribute("loginMember") Member loginMember) {
		return service.selectRoomList(loginMember.getMemberNo());
	}
	
	
	/** 채팅메시지 읽어오기
	 * @param chattingNo
	 * @param loginMember
	 * @return
	 */
	@ResponseBody
	@GetMapping("selectMessage")
	public List<Message> selectMessage(
			@RequestParam("chattingNo") int chattingNo,
			@SessionAttribute("loginMember") Member loginMember){
		return service.selectMessageList(chattingNo, loginMember.getMemberNo());
	}
	
	/** 읽음으로 전환
	 * @param map
	 * @param loginMember
	 * @return
	 */
	@ResponseBody
	@PutMapping("updateReadFlag")
	public int updateReadFlag(
			@RequestBody int chattingNo,
			@SessionAttribute("loginMember") Member loginMember) {
		System.out.println("------");
		return service.updateReadFlag(chattingNo, loginMember.getMemberNo());
	}
	
	
	
}
