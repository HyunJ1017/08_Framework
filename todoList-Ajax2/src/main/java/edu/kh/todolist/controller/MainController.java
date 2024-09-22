package edu.kh.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@RequestMapping("/") // 최상위주소매핑(GET/POST 가리지않음)
	public String toMain(Model model) {
		
		
		return "common/main";
	}
	

}
