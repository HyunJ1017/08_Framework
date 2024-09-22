package edu.kh.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.service.ChatService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RequestMapping("chat")
@Controller
public class ChatController {
	
	@Autowired
	ChatService service;
	
	@PutMapping("insertChat")
	public int insertChat() {
		
		
		return 0;
	}
}
