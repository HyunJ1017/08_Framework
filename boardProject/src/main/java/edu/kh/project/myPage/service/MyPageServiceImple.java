package edu.kh.project.myPage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.dto.Member;
import edu.kh.project.myPage.mapper.MyPageMapper;

@Transactional	// 서비스 내 매서드 수행 중 UnCheckedException 발생 시 rollback 수행, 아니면 메서드 종료시 commit 수행
@Service
public class MyPageServiceImple implements MyPageService{
	
	@Autowired
	private MyPageMapper mapper;

	/**
	 *  회원 정보 수정
	 * @param inputMember : 멤버번호 + 수정할 닉네임, 전화번호, 주소
	 * @return
	 */
	@Override
	public int updateInfo(Member inputMember) {
		
		// 만약 주소가 입력되지 않은 경우 null로 변경
		// "" + "" + "" => '"", "", ""' => ',,'
		if(inputMember.getMemberAddress().equals(",,")) {
			inputMember.setMemberAddress(null);
			// UPDATE 구문 수행 시 MEMBER_ADDRESS 컬럼값이 NULL이 됨
		}
		
		// SQL 수행 후 결과반환
		return mapper.updateInfo(inputMember);
	}

	/**(비동기) 닉네임 중복검사
	 * 
	 * @param input
	 * @return 0 | 1, 0이면 중복아님, 1중복맞음
	 */
	@Override
	public int checkNickname(String input) {
		
		return mapper.checkNickname(input);
	}

}
