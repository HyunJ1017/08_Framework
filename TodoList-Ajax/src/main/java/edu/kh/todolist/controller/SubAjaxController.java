package edu.kh.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todolist.dto.Sub;
import edu.kh.todolist.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	
	/** 세부할일목록 추가
	 * 
	 * @param sub
	 * @return
	 */
	@ResponseBody
	@PostMapping("insert")
	public int insert(@RequestBody Sub sub) {
		log.debug("sub : {}", sub);
		
		return service.insertSub(sub);
	}
	
	/** 완료여부 변경
	 * 
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@PutMapping("completeChange")
	public int completeChange(@RequestBody int subNo) {
		
		return service.completeChangeSub(subNo);
	}
	
	@ResponseBody
	@PutMapping("update")
	public int updateSub(
			@RequestParam("updateSubNo") String updateSubNo,
			@RequestParam("updateValue") String updateValue,
			@RequestParam("updateSubject") String updateSubject ) {
		
		log.debug(updateSubNo + updateValue + updateSubject);
		
		return 0;
	}
	
	

}
