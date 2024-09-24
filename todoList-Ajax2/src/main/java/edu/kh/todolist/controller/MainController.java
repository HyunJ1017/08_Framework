package edu.kh.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todolist.dto.ChatMember;
import edu.kh.todolist.service.ChatService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@Autowired
	ChatService service;
	
	@RequestMapping("/") // 최상위주소매핑(GET/POST 가리지않음)
	public String toMain(Model model) {
		
		List<ChatMember> memberList = service.selectAllMember();
		model.addAttribute("memberList", memberList);
		
		return "common/main";
	}
	

}
