package edu.kh.project.member.service;

import edu.kh.project.member.dto.Member;

public interface MemberService {

	/** 로그인 서비스
	 * 
	 * @param memberEmail
	 * @param memberPw
	 * @return loginMember 또는 null(memberEmail, memberPw 불일치)
	 */
	Member login(String memberEmail, String memberPw);

	/** 회원가입
	 * @param inputMember
	 * @return result
	 */
	int signUp(Member inputMember);

	/** 이메일 중복검사
	 * @param inputEmail
	 * @return count
	 */
	int emailCheck(String inputEmail);

	/** 닉네임 중복검사
	 * @param inputNickname
	 * @return count
	 */
	int duplication(String inputNickname);

}
