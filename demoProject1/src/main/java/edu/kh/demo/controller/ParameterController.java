package edu.kh.demo.controller;

import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.demo.dto.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMethod;



// Parameter : 매개 변수(메서드 수행 시 전달 받은 값)
// Argument  : 전달 인자(메서드 수행 시 전달하는 값)

// Controller : 요청, 응답 제어역할
// Bean       : Spring이 만든 객체
@Controller // 컨트롤러임을 명시 + Bean 등록
@RequestMapping("param") // "/param"으로 시작하는 요청을 해당 컨트롤러에 매핑
@Slf4j
public class ParameterController {
	
	@RequestMapping("main")
	public String paramMain() {
		
		// /templates/param/param-main.html오 forward하기
		
		// * View Resolver (뷰 해결사)가 접두사, 접미사를 붙여서 자동으로 forward 진행
		// thymeleaf의 접두사 : classpath:/templates/
		// thymeleaf의 접미사 :	.html
		
		return "param/param-main";
	}
	
	
	// /param/test1 GET 방식 요청을 처리하는 메서드
//@RequestMapping("path", method=RequestMethod.GET)

	//HttpServletRequest
	// - 요청한 클라이언드의 정보, 제출된 파라미터 등을 저장한 객체
	@GetMapping("test1")
	public String test1(HttpServletRequest req) {
		
		// 매개 변수 HttpServletRequest req 사용 가능할까??	
		// -> 가능!!!
		
		// 왜? Spring의 ArgumentResolver 때문에 가능
		
		// ArgumentResolver(전달 인자 해결사)
		// -> Controller 메서드에 작성된 
		//  매개 변수 타입에 맞는 객체가 존재하면 그대로 바인딩
		//  없으면 생성해서 바인딩
	  // https://docs.spring.io/spring-framework/reference/web/webflux/controller/ann-methods/arguments.html
		
		String inputName = req.getParameter("inputName");
		String inputAge = req.getParameter("inputAge");
		String inputAddress = req.getParameter("inputAddress");
		
		System.out.println(inputName);
		System.out.println(inputAge);
		System.out.println(inputAddress);
		
		// requestScope에 K:"message", V:"메시지입니다" 세팅
		req.setAttribute("message", "메시지입니다");
		
		// classpath:/templates/param/result1.html로 forward
		return "param/result1";

		// forward
		// : 클라이언트 요청에대한 응답처리를 위임하는것
	}
	
	
	//====================================================================================
	
	
	/* 2. @RequestParam 어노테이션 - 낱개(한 개, 단 수)개 파라미터 얻어오기
	 * 
	 * - request객체를 이용한 파라미터 전달 어노테이션 
	 * - 매개 변수 앞에 해당 어노테이션을 작성하면, 매개변수에 값이 주입됨.
	 * - 주입되는 데이터는 매개 변수의 타입이 맞게 형변환/파싱이 자동으로 수행됨!
	 * 
	 * [기본 작성법]
	 * @RequestParam("key") 자료형 매개변수명
	 * 
	 * 
	 * [속성 추가 작성법]
	 * @RequestParam(value="name", required="fasle", defaultValue="1") 
	 * 
	 * value : 전달 받은 input 태그의 name 속성값
	 * 
	 * required : 입력된 name 속성값 파라미터 필수 여부 지정(기본값 true) 
	 * 	-> required = true인 파라미터가 존재하지 않는다면 400 Bad Request 에러 발생 
	 * 	-> required = true인 파라미터가 null인 경우에도 400 Bad Request
	 * 
	 * defaultValue : 파라미터 중 일치하는 name 속성 값이 없을 경우에 대입할 값 지정. 
	 * 	-> required = false인 경우 사용
	 */
	
	
	// "/param/test2" POST방식 요청을 처리하는 메서드
	@PostMapping("test2")
	public String test2(
			@RequestParam("title")     String t,
			@RequestParam("writer")    String w,
			@RequestParam("price")     int p,
			@RequestParam("publisher") String ps
			) {
		
		// parameter 중 price를 int로 저장할 수 있는 이유!
		// html에서 얻어오는 모든 값은 기본적으로 String 형태이나
		// Spring이 매개변수에 기입된 타입으로 자동으로 형변환/파싱을 수행해 준다
		
		
/*		log를 이용해서 파라미터 출력하기
 * 
 * 1. 클래스 위에 @Slf4j 어노테이션 추가 (Lombok 제공)
 * 2. log.debug() || log.info() 등을 이용해서 log 출력
 */
		log.debug("title : {}", t);
		log.debug("writer : {}", w);
		log.debug("price : {}", p);
		log.debug("publisher : {}", ps);
		
		
		// classpath:/templates/param/result2.html
		// forward
		return "param/result2";
	}
	
	
	//====================================================================================
	
	/* 3. @RequestParam 이용하기
  - 여러 개(복수, 다수) 파라미터 컨트롤러에서 얻는 방법 */

// 1) String[]
// 2) List<자료형>
// 3) Map<String, Object>
	
	// /param/test3 POST 방식 처리 메서드
	@PostMapping("test3")
	public String test3(
			@RequestParam(value = "color", required = false) String[] colorArr,
			// 파라미터 중 color만 모아서 String 배열로 전달받기
			// 단, 필수는 아님 == null이면 무시함
			@RequestParam(value="fruit", required = false) List<String> fruitList,
			@RequestParam Map<String, Object> paramMap
			//-> Map 형식으로 파라미터를 얻어오는 경우
			//  == 특정 키만 얻어오는게 아닌 모든 파라미터를 얻어옴
			//     단, 키가 중복인 경우 value 덮어쓰기가 이루어짐
			) {
		
		log.debug("colorArr : {}", Arrays.toString(colorArr));
		
		log.debug("fruitList : {}", fruitList);
		
		log.debug("paramMap : {}", paramMap);
		
		// classpath:/templates/param/result3.html
		// forward
		return "param/result3";
		
	}
	
	// @RequestParam 속성 확인하기
	@GetMapping("test4")
	public String test4(
			//@RequestParam("inputId") String id,
			//@RequestParam(value = "inputId") String id,
			@RequestParam(value = "inputId", required = true) String id,
			//@RequestParam("inputPw") String pw,
			//@RequestParam(name = "inputPw") String pw,
			@RequestParam(name = "inputPw", required = true) String pw,
			//@RequestParam("saveId")  String save
			//@RequestParam(name = "saveId", required = true)  String save
			//@RequestParam(name = "saveId", required = false)  String save	// 체크되지 않을때 null 반환
			@RequestParam(value = "saveId", required = false, defaultValue = "X")  String save	
			) {
		
		/* type="text" 관련 input 태그는 값을 작성하지 않은경우 "" 이 제출된다.
		   -> 제출되는 값이 존재하기 때문에
				     There was an unexpected error (type=Bad Request, status=400).
					   Required parameter 'saveId' is not present.
					오류가 발생하지 않고
					@RequestParam에 아무 속성을 적지 않아도 잘 얻어와 진다.
			 
		 * name 속성 == value 속성(같은속성)
		   
		 * required : 필수
		   -> 기본값이 true
		   -> true : 꼭 제출되어야 하는 파라미터
		   -> 제출되지 않을경우 HTTP 상태코드 400 -> 400 Bad Request 에러 발생
		   -> type="text" 에는 의미 없음 -> ""이 제출되기 때문
		   
		   @checkbox, redio : 체크되지 않으면 제출 X ->> 400에러
		   
		   @직접 쿼리스트링을 작성하는데
		    key=value가 누락된 경우 ->> 400에러
		    
		    required = false 일때, 값이 제출되지 않으면 ->> null
		   
		 * defaultValue :
		   - required = false 이면서, 값이 제출되지 않았을 경우
		   	 매개변수 대입할 값 지정
		 * 
		 */
		
		
		log.debug("inputId : {}", id);
		log.debug("inputPw : {}", pw);
		log.debug("saveId : {}", save);
		
		
		/* Spring에서 redirect 하는 방법 */
		// -> redirect 하려는 요청 주소 앞에 "redirect:" 붙여주면 됨
		return "redirect:/param/main";
	}
	
	
	/* @ModelAttribute를 이용한 파라미터 얻어오기 */
	
	//@ModelAttribute
	// - DTO(또는 VO)와 같이 사용하는 어노테이션
	
	// - 전달 받은 파라미터의 name 속성 값이
	//   같이 사용되는 DTO의 필드명과 같다면
	//   자동으로 setter를 호출해서 필드에 값을 세팅
	
	// *** @ModelAttribute 사용 시 주의사항 ***
	// - DTO에 기본 생성자가 필수로 존재해야 한다!
	// - DTO에 setter가 필수로 존재해야 한다!
	
	// *** @ModelAttribute 어노테이션은 생략이 가능하다! ***
	
	// *** @ModelAttribute를 이용해 값이 필드에 세팅된 객체를
	//		"커맨드 객체" 라고 한다 ***
	
	// 많은 양의 파라미터를 하나의 DTO로 한 번에 저장할 수 있어
	// 굉장히 편함
	
	@PostMapping("test5")
	public String test5(
			@RequestParam("memberId")   String memberId,
			@RequestParam("memberPw")   String memberPw,
			@RequestParam("memberName") String memberName,
			@RequestParam("memberAge")  int    memberAge,
			// -> Key값과 변수명이 같을경우 @RequestParam의 ()생략이 가능했지만
			// 지금 버전은 생략하면 오류 발생 --> 파라미터 명시해야함
			/*@ModelAttribute*/ Member member2 // == 커맨드객체
			) {
		
		log.debug("memberId : {}", memberId);
		log.debug("memberPw : {}", memberPw);
		log.debug("memberName : {}", memberName);
		log.debug("memberAge : {}", memberAge);
		
		// Member 객체를 생성해서 전달받은 값 생성하기
		Member member1 = new Member();
		member1.setMemberId(memberId);
		member1.setMemberPw(memberPw);
		member1.setMemberName(memberName);
		member1.setMemberAge(memberAge);
		
		log.debug("member1 : {}", member1);
		
		//@ModelAttribute를 이용한 파라미터
		log.debug("member2 : {}", member2);
		
		return "redirect:/param/main";
	}

}
