package edu.kh.daemoim.findIdPw.service;

public interface FindIdPwService {

	// 아이디 찾기용 이메일 체크
	int findIdEmailcheck(String email);

	// 비밀번호 찾기용 이메일 체크
	int findPwEmailcheck(String id, String email);
	
	// 아이디 찾기 결과
	String memberId(String email);

	// 비밀번호 찾기 후 비밀번호 변경
	int chagePwAuthKeyPage(String findPwMemberId, String newPw);
	


}