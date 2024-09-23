package edu.kh.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.dto.Chatting;
import edu.kh.todolist.service.ChatService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@RequestMapping("chat")
@Controller
public class ChatController {
	
	@Autowired
	ChatService service;
	
	/** 채팅입력
	 * @return
	 */
	@ResponseBody
	@PutMapping("insertChat")
	public int insertChat(
			@RequestBody Chatting chatting
			) {
		System.out.println("=============================");
		System.out.println("실행");
		System.out.println("getMemberNo : " + chatting.getMemberNo());
		System.out.println("getChatBody : " + chatting.getChatBody());
		System.out.println("=============================");
		
		return service.insertChat(chatting);
	}
	
	/** 패팅창
	 * 
	 * @param param
	 * @return
	 */
	@ResponseBody
	@GetMapping("selectChat")
	public List<Chatting> selectChat() {
		return service.selectChat();
	}
	
	/**
	 * 이름찾기
	 */
	@ResponseBody
	@GetMapping("selectName")
	public String selectName( @RequestParam("memberNo") int memberNo ) {
		String memberName = service.selectName(memberNo);
		System.out.println("=============================");
		System.out.println("memberNo : " + memberNo);
		System.out.println("memberName : " + memberName);
		System.out.println("=============================");
		return memberName;
	}
	
}
