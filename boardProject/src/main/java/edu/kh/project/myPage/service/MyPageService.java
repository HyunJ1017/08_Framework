package edu.kh.project.myPage.service;

import edu.kh.project.member.dto.Member;

public interface MyPageService {

	/**
	 *  회원 정보 수정
	 * @param inputMember : 멤버번호 + 수정할 닉네임, 전화번호, 주소
	 * @return
	 */
	int updateInfo(Member inputMember);

	/**(비동기) 닉네임 중복검사
	 * 
	 * @param input
	 * @return 0 | 1, 0이면 중복아님, 1중복맞음
	 */
	int checkNickname(String input);
	

}
