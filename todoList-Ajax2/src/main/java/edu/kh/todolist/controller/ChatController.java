package edu.kh.todolist.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todolist.dto.ChatBody;
import edu.kh.todolist.dto.Chatting;
import edu.kh.todolist.service.ChatService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("chat")
@Controller
public class ChatController {
	
	@Autowired
	ChatService service;
	
	/** 채팅입력
	 * @param chatting : 전달받은 멤버의 멤버번호, 채팅내용
	 * @return 0 || 1
	 */
	@ResponseBody
	@PutMapping("insertChat")
	public int insertChat(
			@RequestBody Chatting chatting
			) {
		return service.insertChat(chatting);
	}
	
	/** 채팅창 불러오기
	 * @return 채팅 전체목록
	 */
	@ResponseBody
	@GetMapping("selectChat")
	public List<ChatBody> selectChat() {
		return service.selectChat();
	}
	
	/**넥네임찾기
	 * @param memberNo : 전달받은 PK번호
	 */
	@ResponseBody
	@GetMapping("selectName")
	public String selectName( @RequestParam("memberNo") int memberNo ) {
		String memberName = service.selectName(memberNo);
		return memberName;
	}
	
}
