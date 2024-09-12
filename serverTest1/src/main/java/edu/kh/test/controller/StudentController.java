package edu.kh.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.test.dto.Student;

@Controller
public class StudentController {

	@PostMapping("select")
	public String selectStudent(Model model, @ModelAttribute Student student) {
		
		
		model.addAttribute("stdName", student.getStdName());
		model.addAttribute("stdAge", student.getStdAge());
		model.addAttribute("stdAddress", student.getStdAddress());
		return "student/select"; 
	}
	
}
//key값도 틀림 ,
//요청은 /student/select인데? , 
//index.html에서 받아오는 저 key값으로 객체가 만들어 지나?,
//@controller 어노테이션 안되있음