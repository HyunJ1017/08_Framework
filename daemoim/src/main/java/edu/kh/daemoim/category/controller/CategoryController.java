package edu.kh.daemoim.category.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.RequiredArgsConstructor;

@SessionAttributes({"loginMember"})
@Controller // 요청/응답 제어하는 Controller 역할 명시 + Bean 등록
@RequiredArgsConstructor
@RequestMapping("category") 
public class CategoryController {
	
	@GetMapping("category")
	public String category() {
		return "category/category";
	}
	
	
}