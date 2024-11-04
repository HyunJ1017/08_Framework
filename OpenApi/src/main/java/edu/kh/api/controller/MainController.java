package edu.kh.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@PropertySource("classpath:/config.properties")
@Slf4j // Logger log 필드 자동생성(Lombok 제공)
@RequestMapping
@Controller
public class MainController {
	
	// 서비스 키 얻어오기
	@Value("${api.serviceKey.encoding}")
	private String encodingServiceKey;
	@Value("${api.serviceKey.decoding}")
	private String decodingServiceKey;
	
	@GetMapping("/")
	public String mainPage() {
		// 해석된 HTML 코드가 클라이언트에게 전달(응답)
		return "main";
	}
	
	/* ResponseEntity : @ResponseBody + 응답상태코드
	 * 
	 * - HTTP 응답을 표현할 수 있는 객체
	 * - 비동기 응답 서비스(RESTful API)에서 유용하게 사용
	 * 
	 * ? : 아무 자료형이나 들어갈 수 있음
	 */
	@PostMapping("getServiceKey")
	public ResponseEntity<?> getServiceKey(){
		try {
			return new ResponseEntity<String>(encodingServiceKey, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<String>("에러", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
}
