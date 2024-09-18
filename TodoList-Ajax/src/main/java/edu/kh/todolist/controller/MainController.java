package edu.kh.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {
	
	@Autowired
	private TodoService service;
	
	@RequestMapping("/") // 최상위주소매핑(GET/POST 가리지않음)
	public String toMain(Model model) {
		
		List<Todo> todoList = service.todoListFullView();
		int completeCount = service.countCompleteCount();
		
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		int listSize = todoList.size();
		model.addAttribute("listSize", listSize);
		
		// classpath:/templates/common/main.html
		return "common/main";
	}
	
	@PostMapping("common/update")
	public String updateTodo(
			@ModelAttribute Todo todo) {
		log.debug("todo : {}", todo);
		return null;
	}
	

}
