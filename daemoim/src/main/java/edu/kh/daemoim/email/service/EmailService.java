package edu.kh.daemoim.email.service;

import java.util.Map;

public interface EmailService {

	
	/** 이메일 발송 서비스
	 * @param string
	 * @param email
	 * @return
	 */
	int sendEmail(String htmlName, String email);

	
	/** 인증번호
	 * @param map
	 * @return
	 */
	boolean checkAuthKey(Map<String, String> map);












}
