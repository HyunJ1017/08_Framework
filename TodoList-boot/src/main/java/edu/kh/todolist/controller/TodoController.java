package edu.kh.todolist.controller;

import java.util.List;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.todolist.dto.Sub;
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
			@RequestParam("todoDetail") String todoDetail,
			@RequestParam("todoTitle") String todoTitle,
			Model model
			) {
		log.debug("todoTitle  : {}", todoTitle);
		log.debug("todoDetail : {}", todoDetail);
		
		Todo todo = Todo.builder().todoTitle(todoTitle).todoDetail(todoDetail).build();
		
		log.debug(todo+"");
		int result = service.insertTodo(todo);
		
		String message = null;
		if(result>0) message = "할일추가성공";
		else				 message = "유감";
		
		model.addAttribute("message", message);
		
		return "redirect:/";
	}
	
	
	/**할일목록 세부 선택
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
		List<Sub> subList = service.selectSub(listNo);
		
		model.addAttribute("todo", todo);
		model.addAttribute("subList", subList);
		
		return "todo/detail";
	}
	

	/** 완료여부버튼 수정하기
	 * 
	 * @param param
	 * @return
	 */
	@PostMapping("comChange")
	public String completeChange(
			@RequestParam("listNo") int listNo,
			@RequestParam("complete") char complete,
			RedirectAttributes ra
			) {
		
		log.debug("listNo : ", listNo);
		log.debug("complete : ", complete);
		
		if(complete == 'O') complete = 'X';
		else								complete = 'O';
		Todo todo = Todo.builder().listNo(listNo).complete(complete).build();
		
		int result = service.completeChange(todo);
		
		String message = null;
		String path = null;
		if(result>0) {
			message = "변경완료";
			path = "redirect:/todo/detail/" + listNo;
		}
		else				 {
			message = "할 일이 존재하지 않습니다. \n 유감";
			path = "redirect:/";
		}
		
		ra.addFlashAttribute("message", message);
		
		return path;
	}
	
	/** 수정페이지 get
	 * 
	 * @param listNo
	 * @param model
	 * @return
	 */
	@GetMapping("update/{listNo}")
	public String updatePage(
			@PathVariable("listNo") int listNo,
			Model model
			) {
		Todo todo = service.selectTodo(listNo);
		
		model.addAttribute("todo", todo);
		
		return "todo/update";
	}
	
	/** 수정후 수정페이지 post
	 * 
	 * @param todoTlistNoitle
	 * @param todoTitle
	 * @param todoDetail
	 * @param color
	 * @param model
	 * @return
	 */
	@PostMapping("update")
	public String updateTodo(
			@RequestParam("listNo")     int listNo,
			@RequestParam("todoTitle")  String todoTitle,
			@RequestParam("todoDetail") String todoDetail,
			@RequestParam("color")      String color,
			RedirectAttributes ra
			) {
		
		Todo todo = Todo.builder()
										.listNo(listNo)
										.todoTitle(todoTitle)
										.todoDetail(todoDetail)
										.color(color)
										.build();
		log.debug("todo : {}", todo );
		
		int result = service.updateTodo(todo);
		
		String message = null;
		if(result>0) message = "수정완료";
		else				 message = "유감";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:update/" + listNo;
	}
	
	/**삭제하기
	 * 
	 * @param param
	 * @return
	 */
	@GetMapping("delete/{listNo}")
	public String getMethodName(
			@PathVariable("listNo") int listNo,
			RedirectAttributes ra
			) {
		
		int result = service.deleteTodo(listNo);
		
		String message = null;
		if(result>0) message = "삭제완료";
		else				 message = "유감";
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/";
	}
	
	/**완료여부로 정렬하기
	 * @param order 이전에 정렬되어있는 방법 식별할 저장값
	 * @param model
	 * @return
	 */
	@GetMapping("orderby/{orderValue}")
	public String orderComplete(
			@PathVariable("orderValue") String order,
			Model model
			) {
		
		int ordernum = 0;
		
		if(order.equals("X")) {
			order = "O";
			ordernum = 1;
		} else {
			order = "X";
			ordernum = 2;
		}
		
		List<Todo> todoList = service.todoListFullViewOder(ordernum);
		int completeCount = service.countCompleteCount();
		
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		int listSize = todoList.size();
		model.addAttribute("listSize", listSize);
		
		model.addAttribute("order", order);
		
		return "common/main";
	}
	
	
	/** 과제추가하기
	 * 
	 * @param subjectTitle
	 * @param subjectDetail
	 * @return
	 */
	@PostMapping("addSub")
	public String postMethodName(
			@RequestParam("subjectTitle") String subjectTitle,
			@RequestParam("subjectDetail") String subjectDetail,
			@RequestParam("listNo") int listNo,
			Model model
			) {

		Sub sub = Sub.builder().listNo(listNo).subjectTitle(subjectTitle).subjectDetail(subjectDetail).build();
		
		log.debug(sub+"");
		int result = service.insertSub(sub);
		
		String message = null;
		if(result>0) message = "과제추가성공";
		else				 message = "유감";
		
		model.addAttribute("message", message);
		
		return "redirect:/todo/detail/" + listNo;
	}
	
	
	
	

}
