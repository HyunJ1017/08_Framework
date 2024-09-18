package edu.kh.todolist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.todolist.dto.Todo;
import edu.kh.todolist.service.TodoService;
import lombok.extern.slf4j.Slf4j;

/* @RequestBody
 * - 비동기 요청(ajax) 시 전달되는 데이터 중
 *   body 부분에 포함된 요청 데이터를
 *   알맞은 Java 객체 타입으로 바인딩하는 어노테이션
 * 
 * (쉬운 설명)
 * - 비동기 요청 시 body에 담긴 값을
 *   알맞은 타입으로 변환해서 매개 변수에 저장
 * */

/* @ResponseBody
 * - 컨트롤러 메서드의 반환 값을
 *   HTTP 응답 본문에 직접 바인딩하는 역할임을 명시
 *  
 * (쉬운 해석)  
 * -> 컨트롤러 메서드의 반환 값을
 *  비동기 (ajax)요청했던 
 *  HTML/JS 파일 부분에 값을 돌려 보낼 것이다를 명시
 *  
 *  - forward/redirect 로 인식 X
 * */

/* [HttpMessageConverter]
 *  Spring에서 비동기 통신 시
 * - 전달되는 데이터의 자료형
 * - 응답하는 데이터의 자료형
 * 위 두가지 알맞은 형태로 가공(변환)해주는 객체
 * 
 * 					 Java <-> JS
 * - 문자열, 숫자 <-> TEXT
 * - 					Map	<-> JSON
 * - 					DTO <-> JSON
 * 
 * (참고)
 * HttpMessageConverter가 동작하기 위해서는
 * Jackson-data-bind 라이브러리가 필요한데
 * Spring Boot 모듈에 내장되어 있음
 * (Jackson : 자바에서 JSON 다루는 방법 제공하는 라이브러리)
 */


@Slf4j
@RequestMapping("todo")
@Controller
public class TodoAjaxController {
	
	@Autowired
	TodoService service;
	
	
	/** 비동기로 할 일 추가
	 * @param todo : @RequestBody를 이용해서 전달받은 JSON형태(String)의 데이터를 Todo 객체로 변환
	 * @return
	 */
	@ResponseBody
	@PostMapping("add")
	public int todoAdd(	@RequestBody Todo todo ) {
		// 반환형을 알맞은 형태로 변경!!
		
		log.debug("todo : {}", todo);
		
		// 서비스 호출 후 결과 반환받기
		int result = service.insertTodo(todo);
		
		/* 비동기 통신의 목적 : '값' 또는 '화면 일부'만 갱신없이 서버로부터 응답 받고 싶을 때 사용 */
		return result; // service 수행 결과 그대로 반환
	}
	
	
	/**
	 * 
	 * @param todoNo : GET방식 요청은 body가 아닌 주소에 담겨 전달된 "파라미터", @RequestParam 으로 얻어옴
	 * @return 검색된 제목
	 */
	@ResponseBody
	@GetMapping("searchTitle")
	public String searchTitle( @RequestParam("todoNo") int todoNo ) {
		
		String todoTitle = service.searchTitle(todoNo);
		
		// 서비스 결과를 "값"형태 그대로 JS 본문으로 반환
		return todoTitle;
	}
	
	/** 할일 전체 갯수를 비동기 통신 방식으로 조회
	 * 
	 * @return
	 */
	@ResponseBody
	@GetMapping("totalCount")
	public int totalCount() {
		
		return service.totalCount();
	}
	
	/** 완료된 할일 갯수를 조회
	 * 
	 * @param param
	 * @return completeCount
	 */
	@ResponseBody
	@GetMapping("completeCount")
	public int completeCount() {
		return service.countCompleteCount();
	}
	
	/** 할일 조회
	 * 
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@GetMapping("selectTodo")
	public /*String*/ Todo getMethodName( @RequestParam("todoNo") int todoNo ) {
		
		// 반환형 String 인 경우 
		// - java 객체는 JS에서 호환이 안됨
		// -> java에서 JS에 호환될 수 있도록 JSON 형태 데이터를 반화
		
		// return "{\"todoNo\":200,\"todoTitle\":\"테스트제목\",\"todoDetail\":\"테스트내용\"}";
		
		
		// 반환형이 Todo (String 아닌 Object인) 경우
		// -> Java 객체가 반환되면 JS에서 쓸 수 없어서
		// Spring에서 이를 자동으로 변환해줄 
		// "HttpMessageConverter" 객체가 변환해 준다.
		return service.selectTodo(todoNo);
	}
	
	/**비동기 통신으로 전체목록 최신화
	 * @return todoList
	 */
	@ResponseBody // 응답 데이더를 호출한 곳(Ajax-fetch구문)으로 돌려보냄
	@GetMapping("todoList")
	public List<Todo> todoListFullView() {
		return service.todoListFullView();
		
		// 비동기 요청에 대한 응답으로 객체 변환시
		// HttpMessageConverter가 JSON(단일객체) 또는 JSONArray(배열, 컬랙션)형태로 변환
		// "[{"K":"V", "K":"V", "K":"V", "K":"V"}, {"K":"V", "K":"V", "K":"V", "K":"V"}, {"K":"V", "K":"V", "K":"V", "K":"V"} ...]"
	}
	
	/** 할일 상세조회
	 * 
	 * @param todoNo
	 * @return Todo
	 */
	@ResponseBody
	@GetMapping("detail/{todoNo}")
	public Todo selectToto( @PathVariable("todoNo") int todoNo ) {
		
		return service.selectTodo(todoNo);
	}
	
	/** 완료여부 변경
	 * 
	 * @param todoNo
	 * @return
	 */
	@ResponseBody
	@PutMapping("completeChange")
	public int completeChange(@RequestBody int todoNo) {
		
		return service.completeChange(todoNo);
	}
	
	/** 삭제요청
	 * 
	 * @param listNo
	 * @return
	 */
	@ResponseBody
	@DeleteMapping("deleteTodo")
	public int deleteTodo(@RequestBody int listNo) {
		return service.deleteTodo(listNo);
	}
	
	/** 할일 수정
	 * 
	 * @param todo : JSON 데이터가 변환되어 필드에 값이 대입된 객체
	 * @return
	 */
	@ResponseBody
	@PutMapping("updateTodo")
	public int putMethodName( @RequestBody Todo todo ) {
		
		
		return service.updateTodo(todo);
	}
	
	@ResponseBody
	@GetMapping("getDetail/{listNo}")
	public String getDetail( @PathVariable("listNo") int listNo ) {
		
		return service.getDetail(listNo);
	}
	
	

}
