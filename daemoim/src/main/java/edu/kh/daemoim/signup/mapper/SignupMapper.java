package edu.kh.daemoim.signup.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SignupMapper {

	
	// 이메일 체크
	int memberEmailCheck(String email);
	
	// 아이디 체크
	int memberIdCheck(String memberId);
	
	// 닉네임 체크
	int nicknameCheck(String nickname);

	


}
