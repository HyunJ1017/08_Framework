package edu.kh.daemoim.email.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.daemoim.common.util.RedisUtil;
import edu.kh.daemoim.email.service.EmailService;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController {

	
	private final RedisUtil redisUtil;
	
	
	private final EmailService service; 
	
	// 레디스 확인하기
	@ResponseBody
	@GetMapping("redisTest")
	public int redisTest(
			@RequestParam("key") String key,
			@RequestParam("value") String value
			) {
		
		// 전달 받은 key, value를 reids에 set 하기 
		redisUtil.setValue(key,value,60);
		
		return 1;
	}
	/** 인증 번호 발송
	 * @param email : 입력된 이메일
	 * @return 성공 1, 실패 0
	 */
	@ResponseBody
	@PostMapping("sendAuthKey")
	public int sendAuthKey(@RequestBody String email) {
		
		return service.sendEmail("signUpCheck",email);
	}
	
	
	/** 인증번호 확인
	 * @param map : 입력받은 email, authKey가 저장된 map 
	 * 	 		HttpMessageConverter에 의해 JSON -> Map 자동 변환
	 * @return
	 */
	@ResponseBody
	@PostMapping("checkAuthKey")
	public boolean checkAuthKey(
			@RequestBody Map<String, String> map){
		
		return service.checkAuthKey(map);
	
	}
	
	@ResponseBody
	@PostMapping("sendFindIdAuthKey")
	public int checkFindIdAuthKey(
			@RequestBody String email) {
		
		return service.sendEmail("findId",email);
		
	}
	
	@ResponseBody
	@PostMapping("checkfindIdAuthKey")
	public boolean checkfindIdAuthKey(
			@RequestBody Map<String, String> map){
		
		return service.checkAuthKey(map);
	
	}
	
	
	@ResponseBody
	@PostMapping("checkfindPwAuthKey")
	public boolean checkfindPwAuthKey(
			@RequestBody Map<String, String> map){
		
		return service.checkAuthKey(map);
	
	}
	
	
	
	
}
