package edu.kh.daemoim.findIdPw.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FindIdPwMapper {

	int findIdEmailcheck(String email);

	// 비밀번호 찾기용 이메일 체크
	int findPwEmailcheck(@Param("id") String id,@Param("email") String email);

	// 아이디 띄우기
	String memberId(String email);

	// 비밀번호 찾기 후 변경 
	int chagePwAuthKeyPage(
			@Param("findPwMemberId") String findPwMemberId,
			@Param("encPw") String encPw);

}