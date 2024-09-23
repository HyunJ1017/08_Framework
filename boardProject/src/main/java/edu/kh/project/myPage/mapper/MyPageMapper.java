package edu.kh.project.myPage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	
	/**
	 * 비밀번호 변경
	 * @param memberNo : 변경할 멤버의 멤버번호
	 * @param encPw : 암호화된 새 비밀번호
	 * @return
	 */
	int changePw(
			@Param("memberNo") int memberNo,
			@Param("encPw") String encPw);

	/** 회원 탈퇴
	 * 
	 * @param memberPw
	 * @param memberNo
	 * @return
	 */
	int secession( int memberNo );
	
	/* 마이바티스 Mapper 인터페이스 메서드 호출 시
	 * 별도의 어노테이션이 없다면
	 * 첫 번째 매개변수 만 
	 * mapper.xml파일에 전달되는 parameter로 인식이 됨
	 * 
	 * [해결방법]
	 * 1. DTO, 컬랙션을 이용한 새 객체로 생성해서 전달
	 * 2. @Param 어노테이션을 이용해 파라미터로 인식
	 * 
	 * @Param("key") 자료형 변수명
	 * - SQL중 #{key} 자리에 들어갈 값 지정
	 */
	
}
