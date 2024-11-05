package edu.kh.daemoim.chatting.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.kh.daemoim.chatting.dto.Chat;
import edu.kh.daemoim.chatting.service.ChattingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("chat")
@RestController
@RequiredArgsConstructor
public class ChattingController {
	
	private final ChattingService service;
	
	
	/** 채팅창 기본정보 볼러오기
	 * DTO 1 - 필요없음, map에 담아오세요 ㅡㅡ
        로그인회원번호
        모임 List - 회원 수 내림차순, resultMap이용
          모임번호
          모임이름
          메시지 카운트
        마지막 읽은 날자
        마지막 읽은 모임번호 lastReaded
	 * @param memberNo
	 * @return map
	 */
	@GetMapping("getChatInfo")
	public Map<String, Object> getChatInfo(
			@RequestParam("memberNo") int memberNo) {
		return service.getChatInfo(memberNo);
	}
	
	/** 채팅 컨탠츠 가져오기
	 * @return chatList
	 */
	@GetMapping("getContent")
	public List<Chat> getContent(
			@RequestParam("groupNo") int groupNo,
			@RequestParam("loginMemberNo") int memberNo) {
		List<Chat> chatList = service.getContent(groupNo, memberNo); 
		log.info("ChattingController.chatList() : ", chatList.toString());
		return chatList;
	}
	
//	/** 마지막 읽은날자 수정
//	 * @return chatList
//	 */
//	@GetMapping("updateReadDate")
//	public void updateReadDate(
//			@RequestParam("groupNo") int groupNo,
//			@RequestParam("memberNo") int memberNo) {
//		service.updateReadDate(groupNo, memberNo);
//	}

}
