package edu.kh.project.email.service;

import java.util.Map;

public interface EmailService {

	/** 회원가입시 인증번호 이메일 발송 서비스
	 * @param string
	 * @param email : 입력받은 이메일
	 * @return 성공 1, 실패 0
	 */
	int sendEmail(String htmlName, String email);

	/** 인증번호 확인
	 * @param map
	 * @return
	 */
	boolean checkAuthKey(Map<String, String> map);

}
