package edu.kh.project.email.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.common.util.RedisUtil;
import edu.kh.project.email.service.EmailService;


@Controller
@RequestMapping("email")
public class EmailController {
	
	@Autowired
	public RedisUtil redisUtil;
	
	@Autowired
	public EmailService service;
	
	@ResponseBody
	@GetMapping("redisTest")
	public int redisTest(
			@RequestParam("key") String key,
			@RequestParam("value") String value) {
		
		// 전달받은 key, value를 redis에 set하기
		redisUtil.setValue(key, value, 60); // 60초 후에 만료되는 key-value
		
		return 0;
	}
	
	/** 인증 번호 발송
	 * @param memberEmail : 입력된 이메일
	 * @return 성공 1, 실패 0
	 */
	@ResponseBody
	@PostMapping("sendAuthKey")
	public int sendAuthKey(
			@RequestBody String email	) {
		
		return service.sendEmail("signUp", email);
	}
	
	/** 입력받은 인증키 확인
	 * @param map : 입력받은 email, authKey가 저장된 map
	 * 		HttpMessageConverter에 의해 자동으로 JSON -> Map 자동 전환
	 * @return
	 */
	@ResponseBody
	@PostMapping("checkAuthKey")
	public boolean checkAuthKey(
			@RequestBody Map<String, String> map ) {
		return service.checkAuthKey(map);
	}
	

}
