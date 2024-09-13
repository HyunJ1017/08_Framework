package edu.kh.project.myPage.mapper;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.dto.Member;

@Mapper // mapper : xml을 DB랑 연결해서 sql실행결과를 가져옴
public interface MyPageMapper {

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
