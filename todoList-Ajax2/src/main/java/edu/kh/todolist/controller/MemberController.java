package edu.kh.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.service.ChatService;

@SessionAttributes("loginMember")
@RequestMapping("member")
@Controller
public class MemberController {

	@Autowired
	ChatService service;
	
	@PostMapping("login")
	public String login(@ModelAttribute ChatMember member, Model model) {
		
		ChatMember getmember = service.selectMember(member);
		
		if(getmember != null) {
			
			model.addAttribute("loginMember", getmember);
			
			return "/common/chatRoom";
		}
		
		return "/common/main";
	}
}
