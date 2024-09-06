package edu.kh.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.service.TodoService;


@Slf4j
@RequestMapping("todo")
@Controller
public class TodoController {
	
	@Autowired
	private TodoService service;
	
	/** 목록 추가하기
	 * 
	 * @return
	 */
	@PostMapping("add")
	public String insertTodo(
			@RequestAttribute("todoDetail") String todoDetail,
			@RequestAttribute("todoTitle") String todoTitle
			) {
		
		Todo todo = Todo.builder().todoTitle(todoTitle).todoDetail(todoDetail).build();
		
		log.debug(todo+"");
		int result = service.insertTodo(todo);
		
		String message = null;
		if(result>0) message = "할일추가성공";
		else				 message = "유감";
		
		return "";
	}
	
	/**할일목록 선택
	 * 
	 * @param listNo
	 * @param model
	 * @return
	 */
	@GetMapping("detail/{listNo}")
	public String detailList(
			@PathVariable("listNo") int listNo,
			Model model
			) {
		
		Todo todo = service.selectTodo(listNo);
		
		model.addAttribute("todo", todo);
		
		return "todo/detail";
	}
	

}
