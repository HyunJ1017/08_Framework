package edu.kh.daemoim.groupMain.controller;

import org.springframework.stereotype.Controller;

import edu.kh.daemoim.groupMain.service.GroupMainService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class GroupMainController {

	
	private final GroupMainService service;
	
	@GetMapping("groupMain")
	public String groupMain() {
		return "groupMain/main";
	}
	
	
}
