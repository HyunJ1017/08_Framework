package edu.kh.daemoim.signup.service;

import edu.kh.daemoim.myPage.dto.MyPage;

public interface SignupService {

	// 아이디 체크
	int memberIdCheck(String memberId);

	
	// 닉네임
	int nicknameCheck(String nickname);
	
	// 전화번호 체크
	int telCheck(String tel);
	
	//-----------------------------------------



	//회원가입 마지막
	int signUp(MyPage inputMember);


	int emailCheck(String email);

}
