package edu.kh.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todolist.dto.Sub;
import edu.kh.todolist.service.TodoService;

@RequestMapping("sub")
@Controller
public class SubAjaxController {
	
	@Autowired
	TodoService service;
	
	@ResponseBody
	@GetMapping("selectSub/{listNo}")
	public List<Sub> selectSub(
			@PathVariable("listNo") int listNo ){
		
		return service.selectAllSub(listNo);
	}

}
