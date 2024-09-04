package edu.kh.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.demo.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("example")
@Controller
public class ExampleController {
	
	// Servlet/JSP에 존재하는 내장 객체 4종류의 데이터 유지범위(Scope)
	
//		1) page - 현재 페이지
//
//		2) request - 요청받은곳 + 요청 위임(forward)받은곳
//		
//		3) session - 클라이언트가 서버 최초 접속 시 생성,
//								 연결한 브라우저 종료 또는 세션시간 만료까지
//
//		4) apllication - 서버 실행 시 1개만 생성, 서버가 종료될 때 까지 유지
	
	
	/**Model
	 * - org.springframework.ui 패키지
	 * - Spring 에서 데이터를 전달하는 역할의 객체
	 * - 데이터 유지 범위(scope) : 기본 request
	 * - @SessionAttributes 와 함께 사용하면 session scope로 변경
	 * 
	 * 
	 * [Model을 이용해서 값을 세팅하는 방법]
	 * Model.addAttribute("key", value);
	 */
	@GetMapping("ex1")
	public String ex1(
			HttpServletRequest req,
			Model model
			) {
		
		// request scope에 값 세팅
		req.setAttribute("test1", "HttpServletRequest로 세팅한 값");

		// Model을 이용해서 request scope 값 세팅
		model.addAttribute("test2", "Model로 세팅한 값");
		
		// 단일값 세팅
		model.addAttribute("productName", "아이스 아메리카노");
		model.addAttribute("price", 2000);
		
		// 복수값 세팅
		List<String> fruitList = new ArrayList<String>();
		fruitList.add("복숭아");
		fruitList.add("딸기");
		fruitList.add("수박");
		fruitList.add("바나나");
		model.addAttribute("fruitList", fruitList);
		
		
		// DTO 객체를 만들어 Model에 세팅 + 빌더 패턴 사용
		Student std = Student.builder()
													.studentNo("1111")
													.name("짱구")
													.age(15)
													.build();
						// -> 빌더사용 : 일부 초기화시 사용하면 좋음 (ex 17개 값중 5,6개 줏어올때)
		
		log.debug("std : {}", std);
		
		model.addAttribute("std", std);
		
		//---------------------------------------------------------
		
		// DTO 필드에 List가 포함되어 있는 경우
		List<String> hobbyList = new ArrayList<>();
		hobbyList.add("축구");
		hobbyList.add("독서");
		hobbyList.add("코딩공부");
		
		Student std2 = Student.builder()
													.studentNo("2112")
													.name("철수")
													.age(15)
													.hobbyList(hobbyList)
													.build();
		
		log.debug("std2 : {}", std2);
		
		model.addAttribute("std2", std2);
		

		return "ex/result1";
	}
	
	//=====================================================================
	
	/**
	 * 
	 * @param model : Spring에서 데이터를 전달하는 용도의 객체
	 * 							(기본 scope : request)
	 * @return
	 */
	@PostMapping("ex2")
	public String ex2(Model model) {
		
		model.addAttribute("str", "<h1>테스트중입니다...      &times; </h1>");
		
		return "ex/result2";
	}
	

}
